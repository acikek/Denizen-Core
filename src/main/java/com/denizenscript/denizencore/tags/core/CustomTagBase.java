package com.denizenscript.denizencore.tags.core;

import com.denizenscript.denizencore.objects.core.CustomObjectTag;
import com.denizenscript.denizencore.tags.TagManager;

public class CustomTagBase {

    public CustomTagBase() {

        // <--[tag]
        // @attribute <custom_object[<custom-object>]>
        // @returns CustomObjectTag
        // @description
        // Returns a custom object constructed from the input value.
        // Refer to <@link ObjectType CustomObjectTag>.
        // -->
        TagManager.registerTagHandler(CustomObjectTag.class, "custom_object", (attribute) -> {
            if (!attribute.hasParam()) {
                attribute.echoError("Custom_Object tag base must have input.");
                return null;
            }
            return CustomObjectTag.valueOf(attribute.getParam(), attribute.context);
        });
    }
}
