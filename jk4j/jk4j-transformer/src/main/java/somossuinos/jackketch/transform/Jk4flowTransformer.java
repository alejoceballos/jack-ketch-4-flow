package somossuinos.jackketch.transform;

public interface Jk4flowTransformer<FROM, TO> {

    TO transform(final FROM from);

}
