# java-filmorate
Template repository for Filmorate project.
![f](https://app.quickdatabasediagrams.com/#/d/Eq3q4E)

``` mermaid
Film
-
Id PK int
Name string
Description string
ReleaseDate LocalDate
Duration Long
Genres Genre[] FK >- Genres.GenreName
MPARating MPARating FK >- MPARating.RatingName
UserLikes int[] FK >- User.Id

User
-
Id PK int
Email string
Login String
Name NULL String
Birthday LocalDate
Friends int[] FK >- User.Id
UnacceptedFriends int[] FK >- User.Id

FilmList
-
FilmId int PK FK >- Film.Id
Film String

UserList
-
UserID int PK FK >- User.Id
User User

Genres
-
GenreName Genre PK

MPARating 
-
RatingName MPARating PK

```


