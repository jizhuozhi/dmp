package cn.elasticj.dmp.lang;

public enum Op {

    /**
     * {@literal LOAD_ORIGIN, top+1}
     */
    LOAD_ORIGIN,

    /**
     * {@literal LOAD_SLOT slot, top+1}
     */
    LOAD_SLOT,

    /**
     * {@literal STORE_SLOT slot, top-1}
     */
    STORE_SLOT,

    /**
     * {@literal LOAD_SYMBOL symbol, top+1}
     */
    LOAD_SYMBOL,

    /**
     * {@literal STORE_SYMBOL symbol, top-1}
     */
    STORE_SYMBOL,

    /**
     * {@literal REMOVE_SYMBOL symbol}
     */
    REMOVE_SYMBOL,

    /**
     * {@literal ITERATOR_NEW}
     */
    ITERATOR_NEW,

    /**
     * {@literal ITERATOR_HAS_NEXT, top+1}
     */
    ITERATOR_HAS_NEXT,

    /**
     * {@literal ITERATOR_NEXT, top+1}
     */
    ITERATOR_NEXT,

    /**
     * {@literal JUMP pc}
     */
    JUMP,

    /**
     * {@literal JUMP_FALSE npc, top-1}
     */
    JUMP_FALSE,

    /**
     * {@literal ARRAY_NEW, top+1}
     */
    ARRAY_NEW,
    /**
     * {@literal ARRAY_PUSH, top-2}
     */
    ARRAY_PUSH,

    /**
     * {@literal PUSH literal, top+1}
     */
    PUSH,

    /**
     * {@literal NEW, top+1}
     * <p>
     * Push a new generic object to stack top
     */
    NEW,

    /**
     * {@literal GET_FIELD field, top-1}
     * <p>
     * Consume stack top, read field, and then push to stack top
     */
    GET_FIELD,

    /**
     * {@literal PUT_FIELD field, top-2}
     * <p>
     * Consume stack top 2, set field top-1 to top-2
     */
    PUT_FIELD,
}
