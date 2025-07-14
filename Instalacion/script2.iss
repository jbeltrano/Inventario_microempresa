; Script Inno Setup
[Setup]
AppName=Inventario Microempresa
AppVersion=1.0
DefaultDirName={pf}\InventarioMicroempresa
DefaultGroupName=Inventario Microempresa
OutputDir=.\instalador_final
OutputBaseFilename=InstaladorInventario
Compression=lzma
SolidCompression=yes

[Files]
Source: "Inventario_microempresa.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "launcher.vbs"; DestDir: "{app}"; Flags: ignoreversion
; Copiamos la base de datos a Documentos del usuario
Source: "src\Archivos\base_inventario.db"; DestDir: "{userdocs}\InventarioMicroempresa"; Flags: ignoreversion
; Copiamos el resto de archivos de src excepto los archivos específicos
Source: "src\*"; DestDir: "{app}\src"; Flags: ignoreversion recursesubdirs createallsubdirs; Excludes: "Archivos\base_inventario.db,Archivos\Direccion.txt"
Source: "jdk\*"; DestDir: "{app}\jdk"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "LOGO_VF.ico"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
;Name: "{group}\Inventario Microempresa"; Filename: "{app}\launcher.vbs"
;Name: "{commondesktop}\Inventario Microempresa"; Filename: "{app}\launcher.vbs"; Tasks: desktopicon
Name: "{group}\Inventario Microempresa"; Filename: "{app}\launcher.vbs"; IconFilename: "{app}\LOGO_VF.ico"
Name: "{commondesktop}\Inventario Microempresa"; Filename: "{app}\launcher.vbs"; IconFilename: "{app}\LOGO_VF.ico"; Tasks: desktopicon

[Tasks]
Name: "desktopicon"; Description: "Crear acceso directo en el escritorio"; GroupDescription: "Accesos directos:"

[Dirs]
; Crear la carpeta en Documentos si no existe
Name: "{userdocs}\InventarioMicroempresa"

[UninstallDelete]
; Eliminar la base de datos y la carpeta de Documentos al desinstalar
Type: files; Name: "{userdocs}\InventarioMicroempresa\base_inventario.db"
Type: dirifempty; Name: "{userdocs}\InventarioMicroempresa"
Type: files; Name: {app}\src\Archivos\Direccion.txt

[Code]
procedure CurStepChanged(CurStep: TSetupStep);
var
  DireccionFile: string;
  Content: string;
begin
  if CurStep = ssPostInstall then
  begin
    // Crear el archivo direccion.txt con la nueva ruta de la base de datos
    DireccionFile := ExpandConstant('{app}\src\Archivos\Direccion.txt');
    Content := ExpandConstant('{userdocs}\InventarioMicroempresa\base_inventario.db');
    
    // Guardar el contenido en el archivo
    if not SaveStringToFile(DireccionFile, Content, False) then
    begin
      MsgBox('Error al crear el archivo de configuración direccion.txt', mbError, MB_OK);
    end;
  end;
end;