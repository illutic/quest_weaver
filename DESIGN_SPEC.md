# Quest Weaver Design Specification

## 1. Color Palette

The color palette is designed to evoke a "Mystical/Arcane" atmosphere, utilizing Deep Blues, Purples, and Golds. It follows Material Design 3 guidelines with distinct mappings for Light and Dark modes.

### Key Colors

| Role | Color Name | Hex Code (Light) | Hex Code (Dark) | Usage |
| :--- | :--- | :--- | :--- | :--- |
| **Primary** | **Arcane Purple** | `#6750A4` | `#D0BCFF` | Main actions, active states, key branding. |
| **On Primary** | White / Dark Violet | `#FFFFFF` | `#381E72` | Text on Primary buttons/components. |
| **Secondary** | **Mystical Gold** | `#625B71` (Muted) / `#9C6E00` | `#CCC2DC` (Muted) / `#FFD740` | Accents, treasures, high-value highlights. |
| **Tertiary** | **Deep Void Blue** | `#0D47A1` | `#82B1FF` | Background accents, magical effects, unread states. |
| **Background** | Scroll / Void | `#FFFBFE` | `#1C1B1F` | App background. |
| **Surface** | Card / Plate | `#E7E0EC` | `#49454F` | Cards, sheets, dialogs. |

*Note: For iOS "Liquid Glass" style, the Dark Mode Surface colors should have alpha values (e.g., `#1C1B1FCC`) combined with a background blur effect.*

### Platform Specifics
*   **Android:** Use standard opaque colors or Material 3 dynamic color defaults if user prefers, but default to this theme.
*   **iOS:** Use `Color.primary` with `.opacity(0.8)` and `.background(.ultraThinMaterial)` for the liquid glass effect.

## 2. Typography

We prioritize readability for campaign text while using display fonts for immersion.

### Font Family Strategy
*   **Display / Headings:** **"Cinzel"** or **"Merriweather"** (Serif) - Evokes a classic RPG/Fantasy feel.
*   **Body / UI:** **"Lato"** or **"Roboto"** (Sans-Serif) - Ensures legibility for long descriptions and chat.

### Hierarchy (Compose Mapped)

| Token | Style | Weight | Size | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `displayLarge` | Serif | Regular | 57sp | Main Title Screens (e.g., "Quest Weaver") |
| `headlineMedium` | Serif | SemiBold | 28sp | Screen Titles (e.g., "Character Sheet") |
| `titleMedium` | Sans-Serif | Medium | 16sp | Section Headers, Card Titles |
| `bodyLarge` | Sans-Serif | Regular | 16sp | Main text, chat messages, campaign notes |
| `labelMedium` | Sans-Serif | Medium | 12sp | Button text, Chip labels, Metadata |

## 3. Component Hierarchy

The UI is built from atomic components, most of which are shared across platforms via Compose Multiplatform, but some high-level containers are platform-aware.

### Shared Components (Core UI)
These components look identical or nearly identical on both platforms to ensure brand consistency.
*   **`ChatBubble`**: Display text, roll results, or system messages.
    *   *Props*: `Message`, `Sender`, `Type` (Text/Roll).
*   **`DiceChip`**: A small, clickable pill showing a dice type (e.g., "d20") or result.
*   **`StatIndicator`**: A circular or hexagonal indicator for stats (STR, DEX, HP).
*   **`SpellCard`**: A detailed card component with expandable text for spell descriptions.
*   **`QuickActionRow`**: Horizontal scrollable row of `ActionButtons` (Attack, Heal, Roll).

### Platform-Specific / Adaptive Components
These components adapt to the host OS paradigm.
*   **`MainScreenScaffold`**:
    *   **Android**: `Scaffold` with `CenterAlignedTopAppBar` and `NavigationBar` (Bottom).
    *   **iOS**: `ZStack` with `BlurView` tab bar and large title `NavigationView`.
*   **`SettingsDialog`**:
    *   **Android**: `AlertDialog` or `ModalBottomSheet`.
    *   **iOS**: `.sheet(isPresented: ...)` with a blurred background.
*   **`BackNavigation`**:
    *   **Android**: System Back button / Gesture.
    *   **iOS**: Swipe-from-edge gesture.

## 4. UX Flow

### User Journey: "The Weekly Session"

1.  **Launch & Lobby**:
    *   User opens app.
    *   **Landing Screen**: List of recent campaigns.
    *   **Action**: Tap "Curse of Strahd" campaign.
2.  **The Game Room (Main Hub)**:
    *   User lands on the **Chat/Feed** tab.
    *   *Visual*: Stream of recent rolls and messages.
    *   **Action**: DM asks for a Perception check.
3.  **Action & Rolling**:
    *   User taps "Roll" FAB or "Perception" in Quick Actions.
    *   **Dice Tray Overlay**: Appears (bottom sheet).
    *   User taps "d20".
    *   **Feedback**: 3D dice roll animation or simple number spin.
    *   **Result**: Automatically posted to Chat ("Rolled 15 + 3 = 18").
4.  **Reference & Management**:
    *   User needs to check a spell.
    *   **Nav**: Tap "Grimoire" (Spellbook) tab.
    *   **Interaction**: Scroll list, tap "Fireball" to expand `SpellCard`.
    *   **Action**: Tap "Cast" on the card -> Deducts spell slot -> Posts to Chat.

## 5. ASCII Wireframe: Main Game Screen

```text
+--------------------------------------------------+
|  [<]  Campaign Name: Strahd       [Settings]     |  <- Top Bar (Glassy)
+--------------------------------------------------+
|                                                  |
|  [Chat] [Rolls] [Players]                        |  <- Segmented Control (Tabs)
|                                                  |
|  +--------------------------------------------+  |
|  | DM: "You see a dark castle ahead..."       |  |
|  | [Image: Castle.png]                        |  |
|  +--------------------------------------------+  |
|                                                  |
|  +------------------------+                      |
|  | Player 1 (You):        |                      |
|  | I cast Detect Magic!   |                      |
|  | [Roll: d20+3 = 18]     |                      |
|  +------------------------+                      |
|                                                  |
|                                                  |
|           (Chat Stream Area)                     |
|                                                  |
+--------------------------------------------------+
|  [Quick Actions: Attack | Heal | Skill Check]    |  <- Quick Action Row
+--------------------------------------------------+
|  Type a message...               [(+) Add]       |  <- Input Area
+--------------------------------------------------+
|  [Chat]    [Stats]    [Grimoire]    [Notes]      |  <- Bottom Nav (Glassy)
+--------------------------------------------------+
```

## 6. KMP Strategy

To handle platform-specific UX differences while maximizing code sharing, we will use a **Slot-Based Architecture** and **Platform Composition Locals**.

### Strategy: "Shared Content, Native Container"

1.  **Shared UI (`shared/ui`)**:
    *   Create a `GameScreenContent` composable that takes *State* and *Event Callbacks*.
    *   It does **not** contain the Scaffold, TopBar, or BottomBar. It only contains the inner content (Chat list, Action rows).
    *   It accepts a `Modifier` to allow the parent to handle padding/insets.

2.  **Android Implementation (`androidApp`)**:
    *   Use `androidx.compose.material3.Scaffold`.
    *   Pass `GameScreenContent` as the `content` lambda.
    *   Use Material 3 `TopAppBar` and `NavigationBar`.

3.  **iOS Implementation (`iosApp`)**:
    *   Use a SwiftUI `NavigationView` or a custom Compose `Scaffold` wrapper that mimics iOS.
    *   Implement "Liquid Glass" effect using `UIVisualEffectView` wrapped in a Composable (via `UIKitView`) or use native SwiftUI TabView overlaying the Compose `ComposeViewController`.
    *   *Alternative (Pure Compose)*: Build a custom `GlassScaffold` in common code that renders a blur effect (using standard blurred backgrounds if available or platform-specific blur via `expect/actual`).

### Handling Navigation Gestures
*   **Android**: Rely on the Activity's default back handling.
*   **iOS**: The `ComposeViewController` is often embedded in a `UINavigationController`. Ensure the swipe-back gesture is enabled. If using pure Compose Navigation on iOS, implement a custom `SwipeToDismiss` layout for screens.
