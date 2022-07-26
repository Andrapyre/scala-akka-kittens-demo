# Kitten Jacket Matcher Service (a demo project)

This project exposes an api for calculating jackets that can be worn by various kittens. It fetches a kitten by its id (name) and then fetches all jackets from a partner service (the jackets api). It then calculates which jackets could pertain to each kitten and returns the jackets with the total number of jackets that could be worn by each kitten.

### External Services (stubbed in project)

1. Kittens Api
2. Jackets Api

### To get project running

1. Run the following: ```sbt run```
2. Import the postman collection inside the /postman folder into postman.
3. Send the request to get the kitten with the name of "ben". The request should return successfully.

### To test

1. Run the following: ```sbt test```

### Documentation

See the api spec in the /api folder, as well as the postman collection mentioned above.
