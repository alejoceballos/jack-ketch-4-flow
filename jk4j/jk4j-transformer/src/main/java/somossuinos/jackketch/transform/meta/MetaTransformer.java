package somossuinos.jackketch.transform.meta;

public interface MetaTransformer<T> {

    MetaWorkflow transformFrom(final T model);

}
