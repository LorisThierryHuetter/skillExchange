
# Skill Exchange

Skill Exchange is an App that lets people that want to learn and teach skills come together so they can teach each other.


![Logo](https://skill-exchange.loris-huetter.ch/logo.png)


## Acknowledgements

 - [Made by Loris Hütter](https://hul.bm-it.ch)
 - [Landing Page](https://skill-exchange.loris-huetter.ch)
 - [Download corresponding App here (Playstore)](PLACEHOLDER)


## Features

- [ ]  Splash screen with logo on startup
- [ ]  Create profile screen if no profile exists
- [ ]  Generate & store identifierKey on phone
- [ ]  Matching screen with profile information
- [ ]  Matching screen with matched profiles (scrollable)
- [ ]  Navigation to Premium screen and edit profile screen
- [ ]  Functioning edit profile screen


## Screenshots

![App Screenshot](https://skill-exchange.loris-huetter.ch/screenshot1.png) 

![App Screenshot](https://skill-exchange.loris-huetter.ch/screenshot2.png)

![App Screenshot](https://skill-exchange.loris-huetter.ch/screenshot1.png)

## API Reference

#### Create profile

```http
  POST /api/identifierKey=unique_key
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierKey` | `varchar(10)` | **Required**. Profile reference |

#### Get profile

```http
  GET /api/identifierKey=unique_key
```

#### Update profile

```http
  PUT /api/identifierKey=unique_key
```
#### Body content (JSON)
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `fullName`      | `string` | **Required**. Full name of user |
| `occupation` | `string` | **Required**. Occupation of user |
| `skills` | `TEXT` | **Required**. A string list of skills the user can teach, at least one. |
| `interests` | `TEXT` | **Required**. A string list of interests the user is willing to learn, at least one. |



## Running Tests 

To run tests using a PC and a mobile device, 
- open the project in Android Studio

- Navigate to `app/java/ch.lorishuetter.skillexchange` 

- open `MainActivityTests.kt`

- Click on the upper most green arrow to initiate the test



## Tech Stack

**Client:** Android

**Server:** PHP, MySQL

