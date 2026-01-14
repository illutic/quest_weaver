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
    - Features may be structured as a **single module** (e.g., `onboarding`) or **split modules** (
      e.g., `user` with `domain`, `data`, etc.).

## Feature Module Patterns

Features are the building blocks of the application.

### 1. Module Organization

* **Single-Module Feature** (Small/Medium scope):
    * Contains `screens/`, `ViewModel`, `State`, and `Route` in one module.
    * Example: `feature/onboarding`.
* **Multi-Module Feature** (Complex scope):
    * Split into sub-modules like `:domain`, `:data`, and the main feature module (UI).
    * Example: `feature/user`.

### 2. Architectural Patterns

The project follows a **Clean Architecture** approach with **MVVM** (Model-View-ViewModel).

#### UI Layer

* **Screens**: Composable functions located in `screens/` package.
* **Structure**: Follow the **Route > Screen > Content > Component** pattern.
    * `UiRoute` (e.g., `FeatureUiRoute`): Initializes the ViewModel and handles other high-level
      setup.
    * `Screen`: Collects state from ViewModel and handles navigation side effects.
    * `Content`: Stateless composable that accepts state and event callbacks.
    * `Component`: Reusable UI elements.
* **Design System**:
    * Always use components and tokens from the `core.ui` Design System.
  * Refer to [DESIGN_SPEC.md](DESIGN_SPEC.md) for detailed design specifications.
    * If a reusable component is missing, create it in `core.components` (if generic) or within the
      feature's `components` package (if feature-specific) before using it.
* **Animations**: Use animations to make the app feel alive and responsive.
* **ViewModel**:
    * Extends `ViewModel`.
    * Implements `KoinComponent` for dependency injection.
    * Exposes `StateFlow<T>` for UI state.
    * Manages navigation actions.
  * **Must** provide a `createFactory()` method in its `companion object` to allow instantiation
    from the iOS App.
* **State**: Defined as a `data class` or `sealed interface`.
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

## Jetpack Compose Structure

* **Composables**: Should be small, focused, and reusable.
* **State Hoisting**: Hoist state to the `Screen` level composable or ViewModel.
* **Previews**: Provide `@Preview` for composables to facilitate UI development and testing.
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
    proper updates.

### 2. Styling & Design

* **Modifiers**: Create custom ViewModifiers for reusable styles.
* **Extensions**: Use extensions on `View` or `Color` for clean API access to design tokens.

### 3. Previews

* **Mandatory**: All Views must have a `#Preview` (or `PreviewProvider` for older iOS versions).
* **Data**: Use mock data for previews to ensure instant rendering.

### 4. Linting

* **SwiftLint**: All Swift code must adhere to [SwiftLint](https://github.com/realm/SwiftLint)
  rules.
* **Format**: Run `swiftformat .` before committing if configured.

### 5. Interoperability

* **Sealed Classes/Interfaces**: Kotlin `object`s inside sealed classes/interfaces are mapped to
  `SealedInterfaceSubClass.shared` in Swift (e.g., `OnboardingRoute.Welcome` in Kotlin becomes
  `OnboardingRouteWelcome.shared` in Swift).
