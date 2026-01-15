import SwiftUI
import Shared

struct ResourcesView: View {
    let strings: HomeStrings
    let resources: [Resource]
    let onResourceClick: (String) -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: Theme.Dimens.spacing2) {
                ForEach(resources, id: \.id) { resource in
                    ResourceCard(
                        resource: resource,
                        onClick: { onResourceClick(resource.id) }
                    )
                }
            }
            .padding(Theme.Dimens.spacing2)
        }
        .background(Theme.Colors.background.ignoresSafeArea())
        .navigationTitle(strings.usefulResourcesTitle)
        .navigationBarTitleDisplayMode(.inline)
    }
}
