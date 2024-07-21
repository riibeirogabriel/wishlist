Feature: Get a product in client wishlist
  Scenario: Client retrieves a product already in its wishlist
    Given the client has a wishlist with a three products
    When the client retrieves one product in its wishlist
    Then the client receives its product