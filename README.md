# Gamer Matching App

The Gamer Matching App is an Android application that allows gamers to connect with each other based on their favorite games and interests. It utilizes the MVI (Model-View-Intent) architectural pattern to provide a robust and reactive user experience.

## About MVI

MVI (Model-View-Intent) is an architectural pattern that promotes a unidirectional flow of data and actions within an application. It consists of three main components:

- **Model**: Represents the state of the application and contains all the necessary data. In the Gamer Matching App, the model would include user information, matches, chat history, and community details.

- **View**: Represents the user interface and is responsible for rendering the model's state. It observes changes in the model and updates the UI accordingly. The views in the app include the Home, Chat, Community, Marketplace, and Profile screens.

- **Intent**: Represents user actions or events that are dispatched from the View to the Model. Examples of intents in the app include swiping left or right for matching, sending messages in the chat, joining or leaving communities, and purchasing avatars.

The MVI pattern offers several advantages:

- **Predictable State**: With a unidirectional flow of data, the application's state becomes predictable, making it easier to reason about and test.

- **Separation of Concerns**: MVI helps in separating the concerns of the model, view, and user intents, making the codebase more modular and maintainable.

- **Reactive and Reactive UI**: MVI lends itself well to reactive programming paradigms, allowing for efficient handling of state changes and keeping the UI in sync with the data.

- **Testability**: The unidirectional flow of data and clear separation of concerns make it easier to write unit tests for individual components of the application.

## Sample folder structure
- `data`: Contains classes related to data handling and storage.
    - `datastore`: Classes for managing data storage using data stores.
    - `local`: Classes for local data storage, such as SQLite databases or shared preferences.
    - `remote`: Classes for handling remote data retrieval and communication with APIs.

- `di`: Contains classes related to dependency injection.
    - This directory can include Dagger modules, components, or any other DI-related classes.

- `domain`: Contains classes representing the domain layer of the app.
    - This layer typically includes business logic and use case classes.

- `presentation`: Contains classes related to the app's user interface and presentation.
    - This directory can include activities, fragments, adapters, view models, and other UI-related components.

- `session`: Contains classes related to user sessions and authentication.
    - This directory can include session management classes, authentication logic, and related utilities.

- `util`: Contains utility classes or helper functions that can be used throughout the app.

- `websocket`: Contains classes related to WebSocket communication, if applicable.

### Files

- `AuthActivity.kt`: Represents the activity responsible for user authentication and login.

- `BaseActivity.kt`: Represents a base activity that other activities can extend from, providing common functionality and features.

- `GameBuddyApplication.kt`: Represents the application class for your Android app, where you can initialize dependencies, set up global configurations, and manage the app's lifecycle.

- `MainActivity.kt`: Represents the main activity of your app, where the primary user interface and navigation take place.

## Pages

### Home
- Allows users to start matching with other gamers by swiping left or right in additional screen.
- Displays popular games.
- Shows pending friend requests.

### Chat
- Displays the inbox of the last chatted user.
- Shows the last message information, date, and avatar.
- Highlights the most communicated friends.
- Provides a chat screen to communicate with friends.

### Community
- Allows users to like photos and comment on them.
- Provides a list of current communities.
- Allows users to join or leave communities.
- Offers a community detail page to view community information.

### Marketplace
- Enables users to buy new avatars.
- Requires a sufficient balance to make purchases.

### Profile
- Allows users to view and edit their profile data.
- Displays achievements.
- Collecting achievements increases the user's balance.

## Getting Started

To run the app locally, follow these steps:

1. Clone this repository.

```shell
git clone https://github.com/your-username/gamer-matching-app.git
```

2. Add your own google-services.json file to the app/ directory.

## Images

There are few more screens in the master branch. Please check it out.

#### Authentication
<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/login_new.png?raw=true" alt="Login" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/register_new.png?raw=true" alt="Register" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/verification.png?raw=true" alt="Verification" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/set_username.png?raw=true" alt="Set Username" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/set_profile_details.png?raw=true" alt="Set Profile Details" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/select_games.png?raw=true" alt="Select Games" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/select_keywords.png?raw=true" alt="Select Keywords" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/loading.png?raw=true" alt="Loading" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/home.png?raw=true" alt="Home" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/matching.png?raw=true" alt="Matching" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/all_friends.png?raw=true" alt="All Friends" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/in_community.png?raw=true" alt="In Community" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/communities_list.png?raw=true" alt="Community List" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/community_2.png?raw=true" alt="Community" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/chat.png?raw=true" alt="Chat" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/messages.png?raw=true" alt="Messages" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/market.png?raw=true" alt="Marketplace" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/profile.png?raw=true" alt="Profile" width="225"/>

<img src="https://github.com/GameBuddyDevs/GameBuddy-Android/blob/development/screenshots/edit_profile.png?raw=true" alt="Edit Profile" width="225"/>

## Getting Started

To run the app locally, follow these steps:

1. Clone this repository.

```shell
git clone <repo-url>
```	

2. Copy your own google-services.json file to the app/ directory.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvement, please submit a pull request or open an issue in this repository.

## License

This project is licensed under the MIT License.