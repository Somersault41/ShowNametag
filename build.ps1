# Show Nametag (1.20.x) - builds every loader and produces a single jar.
# Usage:  powershell -ExecutionPolicy Bypass -File build.ps1
# Needs: JDK 21 (Forge/NeoForge Gradle) and JDK 25 (Fabric Loom), searched under C:\Program Files\Java.
# The mod itself is compiled for Java 17 (Gradle toolchains download JDK 17 if it is missing).
$ErrorActionPreference = 'Stop'
$ModVersion = '1.0.0'
$McVersion  = '1.20-1.20.6'

$root = $PSScriptRoot
function Find-Jdk([int]$major) {
    $jdk = Get-ChildItem 'C:\Program Files\Java' -Directory | Where-Object Name -match "^jdk-$major(\.|$)" | Select-Object -First 1
    if (-not $jdk) { throw "JDK $major not found under C:\Program Files\Java." }
    $jdk.FullName
}
# Forge's Gradle 8.12 does not run on Java 25; Loom 1.18 wants Java 25.
$gradleJdk = @{ fabric = (Find-Jdk 25); neoforge = (Find-Jdk 21); forge = (Find-Jdk 21) }
$jar   = "$($gradleJdk.forge)\bin\jar.exe"
$javap = "$($gradleJdk.forge)\bin\javap.exe"

foreach ($l in 'fabric', 'neoforge', 'forge') {
    Write-Host "== building $l"
    $env:JAVA_HOME = $gradleJdk[$l]
    Push-Location "$root\loaders\$l"
    & .\gradlew.bat build --no-daemon
    if ($LASTEXITCODE -ne 0) { throw "$l build failed" }
    Pop-Location
}

$work = "$root\.work"
if (Test-Path $work) { Remove-Item $work -Recurse -Force }
$m = "$work\merged"; New-Item -ItemType Directory "$m\META-INF" -Force | Out-Null
foreach ($x in @{ fabric = 'f'; neoforge = 'n'; forge = 'g' }.GetEnumerator()) {
    $d = "$work\$($x.Value)"; New-Item -ItemType Directory $d | Out-Null
    $built = Get-ChildItem "$root\loaders\$($x.Key)\build\libs\*.jar" | Where-Object Name -notlike '*sources*' | Select-Object -First 1
    Push-Location $d; & $jar xf $built.FullName; Pop-Location
}

# The SRG helper must come out of reobfJar with SRG names (Minecraft.getInstance = m_91087_)
$srg = & $javap -c -p -cp "$work\g" com.shownametag.OwnNametagSrg | Out-String
if ($srg -notmatch 'm_91087_') { throw 'OwnNametagSrg was not reobfuscated to SRG names' }

# Forge/NeoForge: shared code with official names (NeoForge build) + SRG helper (Forge build)
Copy-Item "$work\n\com", "$work\n\shownametag.mixins.json", "$work\n\pack.mcmeta" $m -Recurse -Force
Copy-Item "$work\g\com\shownametag\OwnNametagSrg.class" "$m\com\shownametag\" -Force
Copy-Item "$work\n\META-INF\neoforge.mods.toml", "$work\g\META-INF\mods.toml" "$m\META-INF" -Force
Copy-Item "$work\g\com\shownametag\forge" "$m\com\shownametag\" -Recurse -Force
# Fabric: separate classes with intermediary names
Copy-Item "$work\f\com\shownametag\fabric" "$m\com\shownametag\" -Recurse -Force
Copy-Item "$work\f\fabric.mod.json", "$work\f\shownametag.fabric.mixins.json" $m -Force
"Manifest-Version: 1.0`nMixinConfigs: shownametag.mixins.json`n" | Set-Content "$work\mf.txt" -Encoding ascii

$out = "$root\Show_Nametag-Fabric+Forge+NeoForge-$ModVersion-$McVersion.jar"
if (Test-Path $out) { Remove-Item $out }
& $jar cfm $out "$work\mf.txt" -C $m .
Remove-Item $work -Recurse -Force
Write-Host "Done: $out"
