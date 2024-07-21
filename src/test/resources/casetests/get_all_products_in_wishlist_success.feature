Feature: Get all products in client wishlist
  Scenario: Client retrieves all products in its wishlist
    Given the client has a wishlist with its products
    When the client retrieves all products in wishlist
    Then the client receives all products in wishlist