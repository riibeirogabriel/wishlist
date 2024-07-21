Feature: Get a product not inserted in client wishlist
  Scenario: Client retrieves a product not inserted in its wishlist
    Given the client has a wishlist
    When the client retrieves a product non existent in its wishlist
    Then the client receives an error message about non existent product