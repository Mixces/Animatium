package org.visuals.legacy.animatium.util.config;

import dev.isxander.yacl3.api.ConfigCategory;
import org.visuals.legacy.animatium.config.category.Category;

public abstract class Bundle {
	public abstract void install(final ConfigCategory.Builder builder, final Category defaults, final Category config);

	public abstract Bundle booleanEntry(final String name);

	public abstract Bundle floatEntry(final String name, final float min, final float max, final float step);

	public abstract <S extends Enum<S>> Bundle enumEntry(final String name, final Class<S> enumClass);
}
