# delivery-co

start demo by calling the /api/deliveries/setdemo

happy flow:
user login/create user
create address with /api/address/resolveaddress or use an existing one
send address or address id to post /api/timeslot/ with address or /api/timeslot/timeslots/{addressId}
send to post /api/deliveries/assambledelivery the user(should be user id) addressId and timeslotId
courier can change the status of the delivery to in progress, cancel or to completed /api/deliveries/{deliveryId}/start /api/deliveries/{deliveryId}/cancel /api/deliveries/{deliveryId}/completed

couriers can be created but no good api to create timeslot(the working hours with ref to the courier)

postman collection added



TODO:
-tests
-roles
-calculation of timeslot can be a diffrent service with some model to calculate better
-sad flow exceptions and return values
-bug fixing
-api working with id and not objects
-limits for daily deliveries and timeslot use 
-paralleling requests and the holidays requests
-flyway
-spring security
-geoapify sometime returns all of info and some time only bare minimum make, need to handle both.
