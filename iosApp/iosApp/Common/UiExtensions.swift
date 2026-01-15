//
//  UiExtensions.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import UIKit
import Shared

extension UInt64 {
    func toSwiftUIColor() -> SwiftUI.Color {
        let red = Double(IosUiHelper.shared.getRed(color: self))
        let green = Double(IosUiHelper.shared.getGreen(color: self))
        let blue = Double(IosUiHelper.shared.getBlue(color: self))
        let alpha = Double(IosUiHelper.shared.getAlpha(color: self))

        return SwiftUI.Color(red: red, green: green, blue: blue, opacity: alpha)
    }

    func toUIColor() -> UIColor {
        let red = CGFloat(IosUiHelper.shared.getRed(color: self))
        let green = CGFloat(IosUiHelper.shared.getGreen(color: self))
        let blue = CGFloat(IosUiHelper.shared.getBlue(color: self))
        let alpha = CGFloat(IosUiHelper.shared.getAlpha(color: self))

        return UIColor(red: red, green: green, blue: blue, alpha: alpha)
    }
}

extension Float {
    func toCGFloat() -> CGFloat {
        return CGFloat(IosUiHelper.shared.getDpValue(dp: self))
    }
}

extension SharedTextStyle {
    func toFont() -> Font {
        let size = self.fontSize.toCGFloat()

        let weight: Font.Weight
        switch self.fontWeight {
        case 400: weight = .regular
        case 500: weight = .medium
        case 600: weight = .semibold
        case 700: weight = .bold
        default: weight = .regular
        }

        let design: Font.Design
        switch self.fontFamily {
        case "Serif": design = .serif
        case "SansSerif": design = .default
        default: design = .default
        }

        return Font.system(size: size, weight: weight, design: design)
    }
}
