import SwiftUI
import Shared

struct HomeView: View {
    let onNavigateGlobal: (Route) -> Void
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: HomeState = HomeState(isLoading: false)
    
    var body: some View {
        let viewModel: HomeViewModel = viewModelStoreOwner.viewModel(
            factory: HomeViewModel.companion.createFactory()
        )
        
        ZStack {
            Text("Home Screen")
        }
        .task {
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { state in self.state = state ?? HomeState(isLoading: false) }
            )
        }
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView(onNavigateGlobal: { _ in })
    }
}
