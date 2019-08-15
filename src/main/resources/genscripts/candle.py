from jsongen import *

copy([
	('unique/block_model_candle.json', 'assets/{modid}/models/block/{name}_candle.json'),
	('unique/block_item_candle.json', 'assets/{modid}/models/item/{name}_candle.json'),
	('unique/blockstate_candle.json', 'assets/{modid}/blockstates/{name}_candle.json'),
	('drop_table_generic.json', 'data/{modid}/loot_tables/blocks/{name}_candle.json'),

	('unique/recipe_candle.json', 'data/{modid}/recipes/decoration/crafting/{name}_candle.json')
])

import update_drop_tables