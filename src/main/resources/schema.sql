DROP DATABASE IF EXISTS `ticket_master_dev`;
CREATE DATABASE IF NOT EXISTS `ticket_master_dev` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `ticket_master_dev`;

CREATE TABLE Role (
  roleId INT PRIMARY KEY AUTO_INCREMENT,
  role VARCHAR(100) NOT NULL
);
INSERT INTO `ticket_master_dev`.`Role` (`roleId`, `role`) VALUES ('1', 'USER');
INSERT INTO `ticket_master_dev`.`Role` (`roleId`, `role`) VALUES ('2', 'EVENT_MANAGER');
INSERT INTO `ticket_master_dev`.`Role` (`roleId`, `role`) VALUES ('3', 'TICKETING_OFFICER');
INSERT INTO `ticket_master_dev`.`Role` (`roleId`, `role`) VALUES ('4', 'ADMIN');

CREATE TABLE User (
  userId INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE,
  email VARCHAR(255) UNIQUE NOT NULL,
  phoneNumber VARCHAR(255),
  status VARCHAR(255) NOT NULL,
  roleId INT NOT NULL,
  FOREIGN KEY (roleId) REFERENCES Role(roleId)
);
INSERT INTO `ticket_master_dev`.`User` (`userId`, `username`, `email`, `phoneNumber`, `status`, `roleId`) VALUES ('1', 'kankoh', 'kankoh98@gmail.com', '+6593360919', 'ACTIVE', '2');
INSERT INTO `ticket_master_dev`.`User` (`userId`, `username`, `email`, `status`, `roleId`) VALUES ('2', 'NO_ACCOUNT', 'NO_ACCOUNT', 'ACTIVE', '1');
INSERT INTO `ticket_master_dev`.`User` (`userId`, `username`, `email`, `status`, `roleId`) VALUES ('3', 'evenetManagerAccount', 'evenetManagerAccount@gmail.com', 'ACTIVE', '2');
INSERT INTO `ticket_master_dev`.`User` (`userId`, `username`, `email`, `status`, `roleId`) VALUES ('4', 'ticketingOfficerAccount', 'ticketingOfficerAccount@gmail.com', 'ACTIVE', '3');


CREATE TABLE Venue (
  venueId INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE,
  address VARCHAR(255) NOT NULL,
  isDeleted BOOLEAN NOT NULL DEFAULT FALSE
  
);

CREATE TABLE EventGroup (
  eventGroupId INT PRIMARY KEY AUTO_INCREMENT,
  venueId INT NOT NULL, 
  name VARCHAR(255) NOT NULL,
  eventType VARCHAR(100) NOT NULL, 
  bookingAllowed BOOLEAN NOT NULL DEFAULT 1,
  cancellationFee FLOAT NOT NULL,
  description TEXT,
  eventManagerId INT NOT NULL,
  bannerImgUrl VARCHAR(2083), 
  posterImgUrl VARCHAR(2083), 
  status VARCHAR(255) NOT NULL,
  isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (venueId) REFERENCES Venue(venueId),
  FOREIGN KEY (eventManagerId) REFERENCES User(userId)
);

CREATE TABLE EventGroupPricing (
  eventGroupPricingId INT PRIMARY KEY AUTO_INCREMENT,
  eventGroupId INT NOT NULL, 
  category VARCHAR(255) NOT NULL,
  price FLOAT NOT NULL,
  FOREIGN KEY (eventGroupId) REFERENCES EventGroup(eventGroupId)
);

CREATE TABLE EventGroupDetail (
  eventGroupDetailId INT PRIMARY KEY AUTO_INCREMENT,
  dateTime DATETIME NOT NULL,
  eventGroupId INT NOT NULL,
  FOREIGN KEY (eventGroupId) REFERENCES EventGroup(eventGroupId)
);

CREATE TABLE VenueArea (
  venueAreaId INT PRIMARY KEY AUTO_INCREMENT,
  venueId INT NOT NULL,
  noOfSeat INT NOT NULL,
  noOfRow INT NOT NULL,
  noOfCol INT NOT NULL,
  category VARCHAR(255) NOT NULL,
  position 	CHAR(10) NOT NULL,
  isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (venueId) REFERENCES Venue(venueId)
);

CREATE TABLE EventVenueArea (
  eventVenueAreaId INT PRIMARY KEY AUTO_INCREMENT,
  venueId INT NOT NULL,
  eventGroupDetailId INT NOT NULL,
  noOfSeat INT NOT NULL,
  noOfRow INT NOT NULL,
  noOfCol INT NOT NULL,
  category VARCHAR(255) NOT NULL,
  position 	CHAR(10) NOT NULL,
  price FLOAT NOT NULL,
  FOREIGN KEY (venueId) REFERENCES Venue(venueId),
  FOREIGN KEY (eventGroupDetailId) REFERENCES EventGroupDetail(eventGroupDetailId)
);

CREATE TABLE Seat (
  seatId INT PRIMARY KEY AUTO_INCREMENT,
  eventVenueAreaId INT NOT NULL,
  seatCol INT NOT NULL,
  seatRow CHAR(2) NOT NULL,
  category VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
  FOREIGN KEY (eventVenueAreaId) REFERENCES EventVenueArea(eventVenueAreaId)
);

CREATE TABLE Ticket (
  ticketId INT PRIMARY KEY AUTO_INCREMENT,
  eventGroupDetailId INT NOT NULL,
  customerId INT NOT NULL,
  status VARCHAR(255) NOT NULL,
  bookingMethod VARCHAR(255) NOT NULL,
  numberOfGuests INT default 0,
  qrCodeImageUrl VARCHAR(500), 
  FOREIGN KEY (eventGroupDetailId) REFERENCES EventGroupDetail(eventGroupDetailId),
  FOREIGN KEY (customerId) REFERENCES User(userId)
);

CREATE TABLE TicketSeat (
  ticketSeatId INT PRIMARY KEY AUTO_INCREMENT,
  ticketId INT NOT NULL,
  seatId INT NOT NULL,
  FOREIGN KEY (ticketId) REFERENCES Ticket(ticketId),
  FOREIGN KEY (seatId) REFERENCES Seat(seatId)
);

CREATE TABLE Transaction (
  transactionId INT PRIMARY KEY AUTO_INCREMENT,
  ticketId INT NOT NULL,
  amount FLOAT NOT NULL,
  dateTime DATETIME NOT NULL,
  type VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
  paymentIntentId VARCHAR(255),
  FOREIGN KEY (ticketId) REFERENCES Ticket(ticketId)
);


CREATE TABLE EventStatistic (
  eventStatisticId INT PRIMARY KEY AUTO_INCREMENT,
  eventGroupId INT NOT NULL,
  eventManagerId INT NOT NULL,
  totalRevenue FLOAT NOT NULL,
  totalTicketsSold INT NOT NULL,
  totalSeatsSold INT NOT NULL,
  totalAttendance INT NOT NULL,
  version INT NOT NULL,
  lastUpdated DATETIME NOT NULL,
  FOREIGN KEY (eventGroupId) REFERENCES EventGroup(eventGroupId),
  FOREIGN KEY (eventManagerId) REFERENCES User(userId)
);

CREATE TABLE EventCategoryStatistic (
  eventStatisticId INT NOT NULL,
  category VARCHAR(255) NOT NULL,
  revenue FLOAT NOT NULL,
  ticketsSold INT NOT NULL,
  seatsSold INT NOT NULL,
  attendance INT NOT NULL,
  version INT NOT NULL,
  PRIMARY KEY (eventStatisticId, category),
  FOREIGN KEY (eventStatisticId) REFERENCES EventStatistic(eventStatisticId)
);