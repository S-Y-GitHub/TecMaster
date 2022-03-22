import SwiftUI

print(NSApplication.shared.isDarkMode)
extension NSApplication{
    public var isDarkMode: Bool{
        if #available(OSX 10.14,*){
            let name=effectiveAppearance.name
            return name == .darkAqua
        }else{
            return false
        }
    }
}