import SwiftUI

struct CustomTabView: View {
    let props: Props
    @Environment(\.safeAreaEdgeInsets) private var safeAreaEdgeInsets

    var body: some View {
        VStack(spacing: 0) {
            Divider()
                    .padding(.bottom, Style.topDividerPadding)

            HStack(alignment: .bottom, spacing: 0) {
                ForEach(props.items) { item in
                    navItem(item: item, props: props)
                }
            }
        }
                .padding(.bottom, safeAreaEdgeInsets.bottom)
                .padding(.bottom, Style.bottomDividerPadding)
                .background(Style.bgColor)
    }


    @ViewBuilder
    private func navItem(item: BottomNavTabItem, props: Props) -> some View {
        let isSelected = item.type == props.selectedNavBarTab

        HStack(alignment: .bottom, spacing: 0) {
            Spacer()
            HStack(alignment: .bottom) {
                VStack(alignment: .center, spacing: 0) {
                    Image(item.type.icon(isSelected: isSelected)).padding(.bottom, item.type == BottomNavTab.more ? 6 : 0)
//                            .resizable()
//                            .aspectRatio(contentMode: .fill)
//                            .frame(width: Style.Icon.size, height: Style.Icon.size)
                    
                    Text(LocalizedStringKey(item.type.label))
                            .font(Style.Label.font)
                            .fixedSize(horizontal: true, vertical: true)
                            .foregroundColor(isSelected ? Style.Label.accentColor : Style.Label.color)
                            .padding(.bottom, Style.Label.paddingBottom)
                            .padding(.top, Style.Label.paddingTop)
                }
            }
            Spacer()
        }
                .contentShape(Rectangle())
                .onTapGesture(perform: { props.onTap(item.type) })
                .onLongPressGesture(perform: { props.onLongTap(item.type) })
    }
}

extension CustomTabView {
    struct Style {

        static let bgColor = Color.white
        static let topDividerPadding: CGFloat = 8
        static let bottomDividerPadding: CGFloat = 8

        struct Badge {
            static let offset = CGSize(width: 16, height: -2)
            static let bgColor = Color.red
            static let fgColor = Color.white
            static let font: Font = Font.system(size: 13)
            static let paddingHorizontal: CGFloat = 5
        }

        struct Icon {
            static let size: CGFloat = 32
            static let color = Color.blue
            static let accentColor = Color.accentColor
            static let font: Font = .title
        }

        struct Label {
            static let color = Color("Tertiary")
            static let accentColor = Color("Primary")
            static let font: Font = Font.system(size: 12).bold()
            static let paddingBottom: CGFloat = 1
            static let paddingTop: CGFloat = 4
        }
    }
}

extension CustomTabView {
    struct Props {
        let selectedNavBarTab: BottomNavTab
        let items: [BottomNavTabItem]
        let onTap: (BottomNavTab) -> Void
        let onLongTap: (BottomNavTab) -> Void
    }
}
