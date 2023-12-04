INSERT INTO RECIPE (id, name, is_vegetarian, serving, instructions) VALUES
(1, 'recipe test 1', FALSE, 4, 'recipe 1 instructions, boil, oven ...'),
(2, 'recipe test 2', TRUE, 2, 'recipe 2 instructions, chop, airFryer...'),
(3, 'recipe test 3', TRUE, 6, 'recipe 3 instructions, marinate, oven...'),
(4, 'recipe test 4', FALSE, 4, 'recipe 4 instructions, bake, oven...'),
(5, 'recipe test 5', TRUE, 8, 'recipe 5 instructions, roast, reduce...');

INSERT INTO RECIPE_INGREDIENTS (recipe_id, ingredients) VALUES
(1, 'ingredient 1'),
(1, 'ingredient 2'),
(1, 'ingredient 4'),
(2, 'ingredient 2'),
(2, 'ingredient 3'),
(2, 'ingredient 5'),
(3, 'ingredient 1'),
(3, 'ingredient 4'),
(3, 'ingredient 5'),
(4, 'ingredient 7'),
(4, 'ingredient 8'),
(4, 'ingredient 9'),
(5, 'ingredient 3'),
(5, 'ingredient 5'),
(5, 'ingredient 9');