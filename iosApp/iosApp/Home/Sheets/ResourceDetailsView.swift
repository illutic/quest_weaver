import SwiftUI
import Shared

struct ResourceDetailsView: View {
    let resource: Resource?

    var body: some View {
        if let resource = resource {
            ScrollView {
                VStack(alignment: .leading, spacing: Theme.Dimens.spacing2) {

                    // Header Image
                    if let imageUrl = resource.imageUrl {
                        AsyncImage(url: URL(string: imageUrl)) { phase in
                            if let image = phase.image {
                                image.resizable()
                                    .aspectRatio(contentMode: .fill)
                            } else if phase.error != nil {
                                Image(systemName: "photo")
                                    .font(.largeTitle)
                            } else {
                                ProgressView()
                            }
                        }
                        .frame(height: 200)
                        .frame(maxWidth: .infinity)
                        .background(Color.gray.opacity(0.3))
                        .clipped()
                    } else {
                        // Placeholder
                        Rectangle()
                            .fill(Color.gray.opacity(0.3))
                            .frame(height: 200)
                            .overlay(
                                Image(systemName: "photo")
                                    .font(.largeTitle)
                                    .foregroundColor(.gray)
                            )
                    }

                    VStack(alignment: .leading, spacing: Theme.Dimens.spacing2) {
                        Text(resource.description_)
                            .font(Theme.Typography.bodyLarge)
                            .foregroundColor(Theme.Colors.onBackground)
                    }
                    .padding(Theme.Dimens.spacing2)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .navigationTitle(resource.title)
            .navigationBarTitleDisplayMode(.inline)
        } else {
            Text("Resource not found")
        }
    }
}
