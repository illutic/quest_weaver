# Onboarding Feature

The **Onboarding** feature guides new users through the initial setup of Quest Weaver, collecting
their adventurer name and introducing them to the realm.

## Overview

This module implements a sequential onboarding flow consisting of three screens:

1. **Welcome**: Introduction with immersive animations.
2. **Registration**: User name collection with "Smart Username Generation".
3. **Tutorial**: Quick overview of key concepts ("Keep in Mind").

The flow is fully adaptive, supporting both mobile and tablet/desktop layouts with centered content
and maximum width constraints.

## Architecture

The feature follows a **Model-View-ViewModel (MVVM)** pattern and uses **Nested Navigation** to
manage its internal flow.

### Navigation

- **Entry Point**: `OnboardingRoute.Graph`
- **Internal Navigation**: Managed by `OnboardingRoute` using a nested `NavDisplay`.
- **State**: The backstack is managed by `OnboardingViewModel` (`OnboardingState.backStack`),
  allowing the `Scaffold` (and its `LinearProgressIndicator`) to persist and animate smoothly across
  screen transitions.

### ViewModel

- **`OnboardingViewModel`**:
    - Manages navigation state (`backStack`).
    - Manages UI state (`name`, `isRegistered`).
    - Handles business logic:
        - `registerUser(name)`: Saves the user via `SetUserUseCase`.
        - `generateRandomName()`: Generates a fantasy name via `GenerateUsernameUseCase`.

### Domain

- **`GenerateUsernameUseCase`**: Combines fantasy adjectives and nouns to create unique names (
  e.g., "Swift Blade").
- **`SetUserUseCase`**: Persists the user data.

## Screens

### 1. Welcome Screen (`WelcomeScreen.kt`)

- Displays the Quest Weaver logo and welcome text.
- Features staggered entrance animations for the logo, text, and button.

### 2. Registration Screen (`RegistrationScreen.kt`)

- Input field for the user's name.
- **Smart Generator**: Includes a "Random" button to generate fantasy names.
- Validates input before allowing progression.

### 3. Tutorial Screen (`TutorialScreen.kt`)

- Displays a list of "Keep in Mind" tips.
- Completes the onboarding flow.

## Adaptive Design

All screens implement adaptive layouts:

- **Scrollable**: Content is wrapped in `verticalScroll` to support small screens and landscape
  orientation.
- **Width Constrained**: Main content is limited to `widthIn(max = 600.dp)` to prevent stretching on
  large screens.

## Usage

To navigate to the onboarding flow, navigate to `OnboardingRoute.Graph`.

```kotlin
navController.navigate(OnboardingRoute.Graph)
```

## Dependencies

- **Koin**: For dependency injection (`UserModule`).
- **Navigation3**: For composable navigation.
- **Material3**: For UI components.
