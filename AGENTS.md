# Quest Weaver Codebase Guide

This document outlines the project structure and architectural patterns used in the Quest Weaver
codebase.

## Project Structure

Quest Weaver is a **Kotlin Multiplatform (KMP)** project following a modular architecture.

### Top-Level Directories

- **`androidApp` / `iosApp`**: Platform-specific entry points.
- **`core`**: Shared foundational modules used across features.
    - **`common`**: Utilities and shared logic.
    - **`components`**: Reusable UI components.
    - **`database`**: Local storage implementation.
    - **`navigation`**: Navigation system definitions (`Route` interfaces).
    - **`ui`**: Design system and shared UI elements.
- **`feature`**: Standalone functional modules.
    - Features are structured as **split modules** (e.g., `user` with `domain`, `data`, etc.).

## Feature Module Patterns

Features are the building blocks of the application.

### 1. Module Organization

* **Multi-Module Feature** (Complex scope):
    * Split into two modules:
        * **`:core`** - Shared contracts and data (State, Route, Strings, Event, SideEffect). Has
          minimal dependencies (typically just `core.navigation`). Business logic and wiring (
          ViewModel, DI modules). This will be shared with the iOS app.
        * **`:presentation`** - UI layer (Screens, UiRoute composables only). Depends on `:core` and
          core UI modules.
    * Example: `feature/onboarding`, `feature/home`.
* **Domain Module with Sub-Features** (Very Complex scope):
    * When a feature has multiple independent areas, split presentation further:
        * `:core` - Shared models for entire feature
        * `:subfeature1`, `:subfeature2`, etc. - Individual sub-features
    * Each sub-feature follows `:presentation` patterns internally.
    * If a sub-feature has multiple independent areas, split presentation further.
    * Example: `feature/home` (has dashboard, recents, resources submodules).
    * **Shared Resources Rule**: When multiple submodules need access to the same data models (e.g.,
      `Resource`, `GameSession`), create a `:core` module to hold these shared models. All other
      submodules depend on `:core`, never on each other.

### 2. Architectural Patterns

The project follows a **Clean Architecture** approach with **MVI-MVVM** (Model-View-Intent /
Model-View-ViewModel).

#### UI Layer

* **MVI Pattern**:
    * **State**: Immutable data class representing the entire UI state (e.g., `HomeState`).
    * **Event (Intent)**: Sealed interface representing all user actions (e.g.,
      `HomeEvent.OnGameClick`).
    * **Effect**: Sealed interface for one-off side effects (e.g., Navigation, Toasts).
    * **ViewModel**: Handles `onEvent(event: Event)` and provides `State` and `Effect`.

* **Screens**: Composable functions located in `screens/` package.
* **Structure**: Follow the **Route > Screen > Content > Component** pattern.
    * `UiRoute`: Collects state, handles effects, and dispatches events to ViewModel.
    * `Screen`: Stateless composable accepting `State` and event callbacks.
    * `Content`: Stateless layout implementation.
    * `Component`: Reusable UI elements.
* **Design System**:
    * Always use components and tokens from the `core.ui` Design System.
    * Refer to [DESIGN_SPEC.md](DESIGN_SPEC.md) for detailed design specifications.
* **ViewModel**:
    * Extends `ViewModel`.
    * Implements `KoinComponent` for dependency injection.
  * Exposes `StateFlow<State>` for UI state.
  * Exposes `onEvent(event: Event)` for processing user actions.
  * **No Helper Methods**: ViewModels should NOT expose data lookup helpers (e.g.,
    `getResource(id)`). All data must flow through state.
      * **Must** provide a `createFactory()` method in its `companion object` to allow
        instantiation
        from the iOS App.
* **State**: Defined as a `data class`.
* **Event**: Defined as a `sealed interface`.
* **Route**:
    * Defined in a separate file (e.g., `OnboardingRoute.kt`).
    * Sealed interface implementing `gr.questweaver.navigation.Route`.
    * Objects represent specific destinations with `path` and `id`.

#### Domain Layer

* **UseCases**: Encapsulate specific business logic.
    * Located in `domain/usecase`.
    * Injected into ViewModels using Koin.

#### Data Layer (If applicable)

* Repositories and data sources implementing the domain interfaces.

### 3. Key Technologies

* **Dependency Injection**: [Koin](https://insert-koin.io/).
    * Usage: `private val useCase: MyUseCase by inject()` in classes implementing `KoinComponent`.
* **Navigation**: Custom Route-based navigation system.
    * Navigation commands (e.g., `navigateTo`, `navigateBack`) are handled in the ViewModel and
      observed by the UI/Navigation host.
* **Connectivity**: Google's Nearby Connections API.
    * Used to create a local squad for:
        * Connecting to other nearby devices.
        * Sharing media.
        * Realtime chat.
        * Collaborative whiteboard/map drawing.

## Development Guidelines

1. **New Features**: Create a new directory in `feature/`. Use the Single-Module pattern unless the
   complexity warrants splitting `domain`/`data`.
2. **Dependencies**: Declare usage of Core modules (`core.ui`, `core.navigation`) in
   `build.gradle.kts`.
3. **State Management**: Always expose immutable state via `StateFlow`.
4. **Navigation**: define routes strictly in a `Route.kt` file within the feature.
5. **Clean Code**: Maintain clean, maintainable, and scalable code. Follow SOLID principles.
6. **Hardcoded Values**: Avoid hardcoded values (strings, dimensions, colors). Use resources and the
   Design System.
7. **Linting**:
    * Kotlin code must adhere to [Detekt](https://detekt.dev/) rules.
    * Jetpack Compose code must adhere
      to [Detekt Jetpack Compose](https://github.com/mrmans0n/compose-rules) rules.
8. **Version Control**:
    * **Atomic Commits**: Bundle each "logical" change into a separate commit to maintain a clean
      history.
    * **Branching**: If you are on the `main` branch, always create a new branch and check out to
      that branch to work on a feature.
9. **UI Logic**:
    * **No Logic in UI**: Logic (e.g., filtering lists, finding items by ID, complex calculations) *
      *MUST NOT** be present in the UI layers (`Screens`, `Views`, `Composables`).
    * **Move to ViewModel**: delegated such logic to the `ViewModel` or `Domain` layer. The UI
      should only display state provided by the ViewModel.

## Jetpack Compose Structure

* **Composables**: Should be small, focused, and reusable.
* **State Hoisting**: Hoist state to the `Screen` level composable or ViewModel.
* **Previews**: Provide `@Preview` (imported from `androidx.compose.ui.tooling.preview.Preview`) for
  composables to facilitate UI development and testing.
* **Modifiers**: Pass a `modifier` parameter to composables to allow external customization, usually
  as the first optional parameter.

## iOS (SwiftUI) Guidelines

### 1. Structure

* **Views**:
    * Located in feature-specific folders (e.g., `Onboarding/Views`).
    * Split complex views into smaller `subviews` or `components`.
* **ViewModels**:
    * SwiftUI views should observe `ObservableObject` ViewModels (often the Shared KMP ViewModel
      wrapped or used directly if KMP–Swift interop allows).
    * Use `@StateObject` for ownership and `@ObservedObject` for dependency injection.
    * StateFlows exposed by the ViewModel should be consumed using the `subscribe` utility to ensure
      proper updates. When using this utility, provide explicit type in closure like so
      `{ (value: Type) in }`.

### 2. Styling & Design

* **Typography**: Use the Design System for consistent typography.
* **Colors**: Use the Design System for consistent colors.
* **Design Tokens**: Use the Design System for consistent styling.
* **Spacing**: Use the Design System for consistent spacing.
* **Modifiers**: Create custom ViewModifiers for reusable styles.
* **Extensions**: Use extensions on `View` or `Color` for clean API access to design tokens.

### 3. Previews

* **Mandatory**: All Views must have a `#Preview` (or `PreviewProvider` for older iOS versions).
* **Data**: Use mock data for previews to ensure instant rendering.

### 4. Linting

* **Detekt**: All Kotlin code must adhere to [Detekt](https://detekt.dev/) rules. Jetpack Compose
  code must adhere to [Detekt Jetpack Compose](https://github.com/mrmans0n/compose-rules) rules.
* **SwiftLint**: All Swift code must adhere to [SwiftLint](https://github.com/realm/SwiftLint)
  rules.
* **Format**: Run `swiftformat .` before committing if configured.

### 5. Interoperability

* **Sealed Classes/Interfaces**: Kotlin `object`s inside sealed classes/interfaces are mapped to
  `SealedInterfaceSubClass.shared` in Swift (e.g., `OnboardingRoute.Welcome` in Kotlin becomes
  `OnboardingRouteWelcome.shared` in Swift).
* **Data Classes**: Data classes with default constructors should provide a `Default` object in
  their `companion object` for
  easier instantiation in Swift. If a data class doesn't have default parameters, this is not
  necessary. Example:
  ```kotlin
  data class MyDataClass(val value: String = "") {
      companion object {
          val Default = MyDataClass()
      }
  }
  ```
