package cn.elasticj.dmp.lang;

public enum Op {

    /**
     * {@literal FRAME_NEW stacks, top-#stacks}
     */
    FRAME_NEW,

    /**
     * {@literal FRAME_RETURN}
     */
    FRAME_RETURN,

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
     * {@literal PUSH literal, top+!}
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
