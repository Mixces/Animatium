package org.visuals.legacy.animatium.util.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.config.category.Category;

import java.util.ArrayList;
import java.util.List;

public class EntryBundle extends Bundle {
	protected final List<Entry<?>> entries;
	protected final List<Group> groups;
	@Getter
	private final String name;
	protected Class<? extends Category> categoryClass;
	@Getter
	protected Category category;

	public EntryBundle(final Category category, final String name) {
		this.entries = new ArrayList<>();
		this.groups = new ArrayList<>();
		this.category = category;
		this.categoryClass = category == null ? null : category.getClass();
		this.name = name;
	}

	@Override
	public void install(final ConfigCategory.Builder builder, final Category defaults, final Category config) {
		for (final Group group : this.groups) {
			final OptionGroup.Builder groupBuilder = OptionGroup.createBuilder();
			groupBuilder.name(Component.translatable("animatium.category." + this.name + ".group." + group.getName()));
			group.install(groupBuilder, defaults, config);
			builder.group(groupBuilder.build());
		}

		for (final Entry<?> entry : this.entries) {
			builder.option(entry.createOption(defaults, config));
		}
	}

	@Override
	public EntryBundle booleanEntry(final String name) {
		this.entries.add(new BooleanEntry(name));
		return this;
	}

	@Override
	public EntryBundle floatEntry(final String name, final float min, final float max, final float step) {
		this.entries.add(new FloatEntry(name, min, max, step));
		return this;
	}

	@Override
	public <S extends Enum<S>> EntryBundle enumEntry(final String name, final Class<S> enumClass) {
		this.entries.add(new EnumEntry<>(name, enumClass));
		return this;
	}

	public EntryBundle group(final Group group) {
		this.groups.add(group);
		group.category = this.category;
		group.categoryClass = this.categoryClass;
		return this;
	}

	public Iterable<Entry<?>> entries() {
		return this.entries;
	}

	public enum Type {
		BOOLEAN,
		FLOAT,
		ENUM
	}

	public static class Group extends EntryBundle {
		public Group(final String name) {
			super(null, name); // TODO: Fix
		}

		@Override
		public void install(final ConfigCategory.Builder builder, final Category defaults, final Category config) {
			throw new UnsupportedOperationException();
		}

		public void install(final OptionGroup.Builder builder, final Category defaults, final Category config) {
			for (final Entry<?> entry : this.entries) {
				builder.option(entry.createOption(defaults, config));
			}
		}

		@Override
		public EntryBundle group(Group group) {
			throw new UnsupportedOperationException();
		}
	}

	@AllArgsConstructor
	public abstract class Entry<T> {
		public final String name;
		public final Type type;

		public abstract Option<T> createOption(final Category defaults, final Category config);

		public T value() {
			try {
				return (T) categoryClass.getField(this.name).get(category);
			} catch (Exception exception) {
				return null;
			}
		}
	}

	private class BooleanEntry extends Entry<Boolean> {
		public BooleanEntry(final String name) {
			super(name, Type.BOOLEAN);
		}

		@Override
		public Option<Boolean> createOption(Category defaults, Category config) {
			return Category.booleanOption(this.name, defaults, config);
		}
	}

	@Getter
	private class FloatEntry extends Entry<Float> {
		private final float min;
		private final float max;
		private final float step;

		public FloatEntry(final String name, final float min, final float max, final float step) {
			super(name, Type.BOOLEAN);
			this.min = min;
			this.max = max;
			this.step = step;
		}

		@Override
		public Option<Float> createOption(Category defaults, Category config) {
			return Category.floatSliderOption(this.name, defaults, config, this.min, this.max, this.step);
		}
	}

	private class EnumEntry<S extends Enum<S>> extends Entry<Enum<S>> {
		@Getter
		private final Class<?> enumClass;

		public EnumEntry(final String name, final Class<S> enumClass) {
			super(name, Type.ENUM);
			this.enumClass = enumClass;
		}

		@Override
		public Option<Enum<S>> createOption(Category defaults, Category config) {
			return Category.enumOption(this.name, defaults, config, (Class<? extends Enum>) this.enumClass);
		}
	}
}
