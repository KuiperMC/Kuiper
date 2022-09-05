/*
 * ClientboundUpdateRecipe
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.network.packet.play;

import fr.enimaloc.kuiper.network.Packet;
import fr.enimaloc.kuiper.network.data.BinaryWriter;
import fr.enimaloc.kuiper.network.data.SizedStrategy;
import fr.enimaloc.kuiper.network.data.Writeable;
import fr.enimaloc.kuiper.objects.Identifier;
import fr.enimaloc.kuiper.objects.Slot;
import fr.enimaloc.kuiper.utils.SimpleClassDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ClientboundUpdateRecipe extends SimpleClassDescriptor implements Packet.Clientbound {

    private List<Recipe> recipes;

    @Override
    public int id() {
        return 0x67;
    }

    public ClientboundUpdateRecipe recipes(Recipe... recipes) {
        this.recipes = Arrays.asList(recipes);
        return this;
    }

    public ClientboundUpdateRecipe recipes(List<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }

    public Clientbound addRecipe(Recipe recipe) {
        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        recipes.add(recipe);
        return this;
    }

    @Override
    public void write(BinaryWriter writer) {
        writer.writeList(recipes, (w, r) -> {
            w.write(r.type().identifier);
            w.write(r.name);
            w.write(r);
        }, SizedStrategy.VARINT);
    }

    public abstract static class Recipe extends SimpleClassDescriptor implements Writeable {

        private final Identifier name;

        public Recipe(Identifier name) {
            this.name = name;
        }

        public Type type() {
            return Arrays.stream(Type.values())
                         .filter(type -> type.clazz == this.getClass())
                         .findFirst()
                         .orElse(null);
        }

        public Identifier name() {
            return name;
        }

        enum Type {
            CRAFTING_SHAPELESS(CraftingShapeless.class),
            CRAFTING_SHAPED(CraftingShape.class),
            CRAFTING_SPECIAL_ARMORDYE(CraftingSpecialArmordye.class),
            CRAFTING_SPECIAL_BOOKCLONING(CraftingSpecialBookcloning.class),
            CRAFTING_SPECIAL_MAPCLONING(CraftingSpecialMapcloning.class),
            CRAFTING_SPECIAL_MAPEXTENDING(CraftingSpecialMapextending.class),
            CRAFTING_SPECIAL_FIREWORK_ROCKET(CraftingSpecialFireworkRocket.class),
            CRAFTING_SPECIAL_FIREWORK_STAR(CraftingSpecialFireworkStar.class),
            CRAFTING_SPECIAL_FIREWORK_STAR_FADE(CraftingSpecialFireworkStarFade.class),
            CRAFTING_SPECIAL_REPAIRITEM(CraftingSpecialRepairitem.class),
            CRAFTING_SPECIAL_TIPPEDARROW(CraftingSpecialTippedarrow.class),
            CRAFTING_SPECIAL_BANNERDUPLICATE(CraftingSpecialBannerduplicate.class),
            CRAFTING_SPECIAL_BANNERADDPATTERN(CraftingSpecialBanneraddpattern.class),
            CRAFTING_SPECIAL_SHIELDDECORATION(CraftingSpecialShielddecoration.class),
            CRAFTING_SPECIAL_SHULKERBOXCOLORING(CraftingSpecialShulkerboxcoloring.class),
            CRAFTING_SPECIAL_SUSPICIOUSSTEW(CraftingSpecialSuspiciousstew.class),
            SMELTING(Smelting.class),
            BLASTING(Blasting.class),
            SMOKING(Smoking.class),
            CAMPFIRE_COOKING(CampfireCooking.class),
            STONECUTTING(Stonecutting.class),
            SMITHING(Smithing.class);

            public final Identifier identifier;
            public final Class<? extends Recipe> clazz;

            Type(Class<? extends Recipe> clazz) {
                this.identifier = Identifier.minecraft(name().toLowerCase());
                this.clazz      = clazz;
            }

            Type(Identifier identifier, Class<? extends Recipe> clazz) {
                this.identifier = identifier;
                this.clazz      = clazz;
            }
        }

        static class CraftingShapeless extends Recipe {
            private Identifier group;
            private List<Ingredient> ingredients;
            private Slot result;

            public CraftingShapeless(Identifier name) {
                super(name);
            }

            public CraftingShapeless group(Identifier group) {
                this.group = group;
                return this;
            }

            public CraftingShapeless ingredients(
                    List<Ingredient> ingredients
            ) {
                this.ingredients = ingredients;
                return this;
            }

            public CraftingShapeless addIngredient(Ingredient ingredient) {
                if (this.ingredients == null) {
                    this.ingredients = new ArrayList<>();
                }
                this.ingredients.add(ingredient);
                return this;
            }

            public CraftingShapeless result(Slot result) {
                this.result = result;
                return this;
            }

            @Override
            public void write(BinaryWriter writer) {
                writer.write(group);
                writer.writeList(ingredients, BinaryWriter::write, SizedStrategy.VARINT);
                writer.write(result);
            }
        }

        static class CraftingShape extends Recipe {
            private int width;
            private int height;
            private Identifier group;
            private Ingredient[] ingredients;
            private Slot result;

            public CraftingShape(Identifier name) {
                super(name);
            }

            public CraftingShape width(int width) {
                this.width = width;
                return this;
            }

            public CraftingShape height(int height) {
                this.height = height;
                return this;
            }

            public CraftingShape group(Identifier group) {
                this.group = group;
                return this;
            }

            public CraftingShape ingredients(
                    Ingredient[] ingredients
            ) {
                if (ingredients.length != width * height) {
                    throw new IllegalArgumentException("Invalid ingredients length");
                }
                this.ingredients = ingredients;
                return this;
            }

            public CraftingShape result(Slot result) {
                this.result = result;
                return this;
            }

            @Override
            public void write(BinaryWriter writer) {
                writer.writeVarInt(width);
                writer.writeVarInt(height);
                writer.write(group);
                for (Ingredient ingredient : ingredients) {
                    ingredient.write(writer);
                }
                result.write(writer);
            }
        }

        static class CraftingSpecialArmordye extends Recipe {
            public CraftingSpecialArmordye(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialBookcloning extends Recipe {
            public CraftingSpecialBookcloning(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialMapcloning extends Recipe {
            public CraftingSpecialMapcloning(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialMapextending extends Recipe {
            public CraftingSpecialMapextending(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialFireworkRocket extends Recipe {
            public CraftingSpecialFireworkRocket(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialFireworkStar extends Recipe {
            public CraftingSpecialFireworkStar(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialFireworkStarFade extends Recipe {
            public CraftingSpecialFireworkStarFade(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialRepairitem extends Recipe {
            public CraftingSpecialRepairitem(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialTippedarrow extends Recipe {
            public CraftingSpecialTippedarrow(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialBannerduplicate extends Recipe {
            public CraftingSpecialBannerduplicate(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialBanneraddpattern extends Recipe {
            public CraftingSpecialBanneraddpattern(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialShielddecoration extends Recipe {
            public CraftingSpecialShielddecoration(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialShulkerboxcoloring extends Recipe {
            public CraftingSpecialShulkerboxcoloring(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class CraftingSpecialSuspiciousstew extends Recipe {
            public CraftingSpecialSuspiciousstew(Identifier name) {
                super(name);
            }

            @Override
            public void write(BinaryWriter binaryWriter) {}
        }

        static class Smelting extends Recipe {
            private Identifier group;
            private Ingredient ingredient;
            private Slot result;
            private float experience;
            private int cookingTime;

            public Smelting(Identifier name) {
                super(name);
            }

            public Smelting group(Identifier group) {
                this.group = group;
                return this;
            }

            public Smelting ingredient(
                    Ingredient ingredient
            ) {
                this.ingredient = ingredient;
                return this;
            }

            public Smelting result(Slot result) {
                this.result = result;
                return this;
            }

            public Smelting experience(float experience) {
                this.experience = experience;
                return this;
            }

            public Smelting cookingTime(int cookingTime) {
                this.cookingTime = cookingTime;
                return this;
            }

            @Override
            public void write(BinaryWriter writer) {
                writer.write(group);
                writer.write(ingredient);
                writer.write(result);
                writer.writeFloat(experience);
                writer.writeVarInt(cookingTime);
            }
        }

        static class Blasting extends Recipe {
            private Identifier group;
            private Ingredient ingredient;
            private Slot result;
            private float experience;
            private int cookingTime;

            public Blasting(Identifier name) {
                super(name);
            }

            public Blasting group(Identifier group) {
                this.group = group;
                return this;
            }

            public Blasting ingredient(
                    Ingredient ingredient
            ) {
                this.ingredient = ingredient;
                return this;
            }

            public Blasting result(Slot result) {
                this.result = result;
                return this;
            }

            public Blasting experience(float experience) {
                this.experience = experience;
                return this;
            }

            public Blasting cookingTime(int cookingTime) {
                this.cookingTime = cookingTime;
                return this;
            }

            @Override
            public void write(BinaryWriter writer) {
                writer.write(group);
                writer.write(ingredient);
                writer.write(result);
                writer.writeFloat(experience);
                writer.writeVarInt(cookingTime);
            }
        }

        static class Smoking extends Recipe {
            private Identifier group;
            private Ingredient ingredient;
            private Slot result;
            private float experience;
            private int cookingTime;

            public Smoking(Identifier name) {
                super(name);
            }

            public Smoking group(Identifier group) {
                this.group = group;
                return this;
            }

            public Smoking ingredient(
                    Ingredient ingredient
            ) {
                this.ingredient = ingredient;
                return this;
            }

            public Smoking result(Slot result) {
                this.result = result;
                return this;
            }

            public Smoking experience(float experience) {
                this.experience = experience;
                return this;
            }

            public Smoking cookingTime(int cookingTime) {
                this.cookingTime = cookingTime;
                return this;
            }

            @Override
            public void write(BinaryWriter writer) {
                writer.write(group);
                writer.write(ingredient);
                writer.write(result);
                writer.writeFloat(experience);
                writer.writeVarInt(cookingTime);
            }
        }

        static class CampfireCooking extends Recipe {
            private Identifier group;
            private Ingredient ingredient;
            private Slot result;
            private float experience;
            private int cookingTime;

            public CampfireCooking(Identifier name) {
                super(name);
            }

            public CampfireCooking group(Identifier group) {
                this.group = group;
                return this;
            }

            public CampfireCooking ingredient(
                    Ingredient ingredient
            ) {
                this.ingredient = ingredient;
                return this;
            }

            public CampfireCooking result(Slot result) {
                this.result = result;
                return this;
            }

            public CampfireCooking experience(float experience) {
                this.experience = experience;
                return this;
            }

            public CampfireCooking cookingTime(int cookingTime) {
                this.cookingTime = cookingTime;
                return this;
            }

            @Override
            public void write(BinaryWriter writer) {
                writer.write(group);
                writer.write(ingredient);
                writer.write(result);
                writer.writeFloat(experience);
                writer.writeVarInt(cookingTime);
            }
        }

        static class Stonecutting extends Recipe {

            private Identifier group;
            private Ingredient ingredient;
            private Slot result;

            public Stonecutting(Identifier name) {
                super(name);
            }

            public Stonecutting group(Identifier group) {
                this.group = group;
                return this;
            }

            public Stonecutting ingredient(
                    Ingredient ingredient
            ) {
                this.ingredient = ingredient;
                return this;
            }

            public Stonecutting result(Slot result) {
                this.result = result;
                return this;
            }

            @Override
            public void write(BinaryWriter binaryWriter) {
                binaryWriter.write(group);
                binaryWriter.write(ingredient);
                binaryWriter.write(result);
            }
        }

        static class Smithing extends Recipe {
            private Ingredient base;
            private Ingredient addition;
            private Slot result;

            public Smithing(Identifier name) {
                super(name);
            }

            public Smithing base(Ingredient base) {
                this.base = base;
                return this;
            }

            public Smithing addition(Ingredient addition) {
                this.addition = addition;
                return this;
            }

            public Smithing result(Slot result) {
                this.result = result;
                return this;
            }

            @Override
            public void write(BinaryWriter writer) {
                writer.write(base);
                writer.write(addition);
                writer.write(result);
            }
        }

        record Ingredient(Slot... items) implements Writeable {

            @Override
            public void write(BinaryWriter binaryWriter) {
                binaryWriter.writeVarInt(items.length);
                for (Slot item : items) {
                    binaryWriter.write(item);
                }
            }

            @Override
            public String toString() {
                return "Ingredient{" +
                       "items=" + Arrays.toString(items) +
                       '}';
            }
        }
    }
}
