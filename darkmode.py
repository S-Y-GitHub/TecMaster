import winreg
path=r"Software\Microsoft\Windows\CurrentVersion\Themes\Personalize"
key=winreg.OpenKeyEx(winreg.HKEY_CURRENT_USER,path)
data,regtype=winreg.QueryValueEx(key,"AppsUseLightTheme")
print(data==0)