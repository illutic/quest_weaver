import SwiftUI
import Shared

struct ResourcesView: View {
    let strings: HomeStrings
    let resources: [Resource]
    let onResourceClick: (String) -> Void

    var body: some View {
        ScrollView {
            LazyVGrid(columns: [GridItem(.adaptive(minimum: 340), spacing: Theme.Dimens.spacing2)], spacing: Theme.Dimens.spacing2) {
                ForEach(resources, id: \.id) { resource in
                    ResourceCard(
                        resource: resource,
                        onClick: { onResourceClick(resource.id) }
                    )
                }
            }
            .padding(Theme.Dimens.spacing2)
        }
        .navigationTitle(strings.usefulResourcesTitle)
        .navigationBarTitleDisplayMode(.inline)
    }
}
