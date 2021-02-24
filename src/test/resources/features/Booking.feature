Feature:  Complete Create Read Update Delete steps for the booking feature

  Scenario: Create a Booking
    Given User's booking information
    When  User creates a booking
    Then  The booking should be successful


  Scenario: Update Booking information
    Given User with valid booking
    When User updates information for existing booking
    Then Verify the information updated successfully

  Scenario: User cannot update an invalid booking
    When user updates information to an invalid booking
    Then user should see the error message


  Scenario: List all bookings
    When User list all booking information
    Then All the booking information should be returned

  Scenario Outline: search bookings by name
    Given User with existing booking
    When User search for booking with name "<filter>"
    Then All returned result should have "<result>"
    Examples:
      | filter                           | result                         |
      | firstname=Erfan                  | firstname=Erfan                |
      | lastname=Oghuz                   | lastname=Oghuz                 |
      | firstname=Erfan&lastname=Oghuz   | firstname=Erfan&lastname=Oghuz |
      | firstname=invalid                | []                             |
      | lastname=invalid                 | []                             |
      | firstname=valid&lastname=invalid | []                             |


  Scenario Outline: search bookings by date
    Given User with existing booking
    When User search for booking with booking "<date>"
    Then All returned result should have booking "<result>"

    Examples:
      | date                                   | result                                 |
      | checkin=2021-02-23&checkout=2021-02-24 | checkin=2021-02-23&checkout=2021-02-24 |
      | checkin=1999-03-13&checkout=2000-05-21 | []                                     |
      | checkin=2019-03-13&checkout=2000-05-21 | []                                     |

  Scenario Outline: Delete Booking with valid ID
    Given User with valid booking
    When User check booking information via "<Condition>" id
    And User delete the booking with the id
    Then User should able to delete the booking
    Examples:
      | Condition |
      | Valid     |
      | Invalid   |