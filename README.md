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
