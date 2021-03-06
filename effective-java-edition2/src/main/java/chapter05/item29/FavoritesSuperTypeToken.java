package chapter05.item29;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesSuperTypeToken {

	private Map<TypeReference<?>, Object> favorites = new HashMap<>();

	public <T> void putFavorite(TypeReference<T> type, T instance) {
		if (type == null) throw new NullPointerException("Type is null");
		favorites.put(type, instance);
	}

	public <T> T getFavorite(TypeReference<T> type) {
		if(type.type instanceof Class<?>)
			return ((Class<T>) type.type).cast(favorites.get(type));
		else
			return ((Class<T>)((ParameterizedType) type.type).getRawType()).cast(favorites.get(type));
	}

	static class TypeReference<T> {
		Type type;

		public TypeReference() {
			Type sType = getClass().getGenericSuperclass();
			if (sType instanceof ParameterizedType) {
				this.type = ((ParameterizedType) sType).getActualTypeArguments()[0];
			} else throw new RuntimeException();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass().getSuperclass() != o.getClass().getSuperclass()) return false;
			TypeReference<?> that = (TypeReference<?>) o;
			return type != null ? type.equals(that.type) : that.type == null;
		}

		@Override
		public int hashCode() {
			return type != null ? type.hashCode() : 0;
		}
	}

	public static void main(String[] args) {
		FavoritesSuperTypeToken f = new FavoritesSuperTypeToken();

		f.putFavorite(new TypeReference<String>(){}, "Java");
		f.putFavorite(new TypeReference<Integer>(){}, 1);
		f.putFavorite(new TypeReference<List<Integer>>(){}, Arrays.asList(1,2,3));
		f.putFavorite(new TypeReference<List<String>>(){}, Arrays.asList("a","b","c"));

		System.out.println(f.getFavorite(new TypeReference<String>(){}));
		System.out.println(f.getFavorite(new TypeReference<Integer>(){}));
		System.out.println(f.getFavorite(new TypeReference<List<Integer>>(){}));
		System.out.println(f.getFavorite(new TypeReference<List<String>>(){}));
	}
}