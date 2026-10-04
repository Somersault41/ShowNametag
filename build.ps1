# Show Nametag (1.21.x) - tum yukleyicileri derler ve tek jar uretir.
# Kullanim:  powershell -ExecutionPolicy Bypass -File build.ps1
# Gereken: JDK 21+ (JAVA_HOME ayarli degilse C:\Program Files\Java altinda aranir).
$ErrorActionPreference = 'Stop'
$ModVersion = '1.0.0'
$McVersion  = '1.21-1.21.11'

$root = $PSScriptRoot
if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    $jdk = Get-ChildItem 'C:\Program Files\Java' -Directory | Where-Object Name -match '^jdk-(2[1-9]|[3-9]\d)' | Sort-Object Name -Descending | Select-Object -First 1
    if (-not $jdk) { throw 'JDK 21+ bulunamadi. JAVA_HOME ayarla.' }
    $env:JAVA_HOME = $jdk.FullName
}
$jar = "$env:JAVA_HOME\bin\jar.exe"

foreach ($l in 'fabric', 'neoforge', 'forge') {
    Write-Host "== $l derleniyor"
    Push-Location "$root\loaders\$l"
    & .\gradlew.bat build --no-daemon
    if ($LASTEXITCODE -ne 0) { throw "$l derlemesi basarisiz" }
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
# Forge/NeoForge: ortak kod (Mojang isimleri)
Copy-Item "$work\n\com", "$work\n\shownametag.mixins.json", "$work\n\pack.mcmeta" $m -Recurse -Force
Copy-Item "$work\n\META-INF\neoforge.mods.toml", "$work\g\META-INF\mods.toml" "$m\META-INF" -Force
Copy-Item "$work\g\com\shownametag\forge" "$m\com\shownametag\" -Recurse -Force
# Fabric: intermediary isimli ayri siniflar
Copy-Item "$work\f\com\shownametag\fabric" "$m\com\shownametag\" -Recurse -Force
Copy-Item "$work\f\fabric.mod.json", "$work\f\shownametag.fabric.mixins.json" $m -Force
"Manifest-Version: 1.0`nMixinConfigs: shownametag.mixins.json`n" | Set-Content "$work\mf.txt" -Encoding ascii

$out = "$root\Show_Nametag-Fabric+Forge+NeoForge-$ModVersion-$McVersion.jar"
if (Test-Path $out) { Remove-Item $out }
& $jar cfm $out "$work\mf.txt" -C $m .
Remove-Item $work -Recurse -Force
Write-Host "Hazir: $out"
