# Online Car-Shop
This project works as an online tool for listing and buying used or secondhand cars. Users can login/register to view, browse, or list their own vehicles. These can be browsed by other users, and they can also see the sellers' contact information.

Frontend: Typescript, ReactJs

Backend: Java Spring

## Backend
The backend, constructed with Java Spring, serves both as a functional system and as a foundational template for new projects. Utilizing a 3-tier architecture, it revolves around domain models, focusing on users' car listings and associated images.
Authentication operations, including login and registration, are facilitated through Spring Security, employing JWT Tokens for authorization filtering. Template interceptors, such as logger and role-checking interceptors, demonstrate potential functionalities for future implementations.
Moreover, scheduled tasks, mirroring the structure of interceptors, offer templates for tasks such as database backup creation and generation of statistics from HTTP requests.

## Build and Run
To build and run the application just use

`gradle bootRun`
