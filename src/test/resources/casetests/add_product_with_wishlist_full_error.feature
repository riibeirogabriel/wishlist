Feature: Add product in full wishlist
  Scenario: Client adds a product in full wishlist
    Given the client has a wishlist with the maximum amount of products allowed
    When the client inserts a product in wishlist
    Then the client receives an error message about the wishlist being full