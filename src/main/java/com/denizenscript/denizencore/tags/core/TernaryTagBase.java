package com.denizenscript.denizencore.tags.core;

import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.Deprecations;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class TernaryTagBase {

    public TernaryTagBase() {
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                ternaryTag(event);
            }
        }, "tern", "ternary");
    }

    // <--[tag]
    // @attribute <tern[<condition>].pass[<result>].fail[<result>]>
    // @returns ObjectTag
    // @description
    // Returns either the 'pass' input, or 'fail' input depending on the outcome of the condition.
    // The 'pass' input will be returned when the condition returns 'true', otherwise the 'fail' input will be returned.
    // Example: '<tern[<player.is_spawned>].pass[Player is spawned!].fail[Player is not spawned!]>'
    // Consider instead using <@link tag ElementTag.if_true.if_false>
    // -->
    public void ternaryTag(ReplaceableTagEvent event) {
        if (!event.matches("ternary", "tern")) {
            return;
        }

        Attribute attribute = event.getAttributes();

        // Fallback if nothing to evaluate
        if (!attribute.hasParam()) {
            return;
        }

        String result = attribute.getParam();

        if (result.equalsIgnoreCase("true")) {
            ObjectTag passValue;
            if (event.hasValue()) {
                Deprecations.oldTernTag.warn(attribute.context);
                passValue = new ElementTag(event.getValue().trim());
                attribute = attribute.fulfill(1);
            }
            else if (attribute.hasContext(2)) {
                passValue = attribute.getContextObject(2);
                attribute = attribute.fulfill(3);
            }
            else {
                Debug.echoError("Ternary tag missing 'pass' value!");
                return;
            }
            event.setReplacedObject(passValue.getObjectAttribute(attribute));
        }
        else {
            ObjectTag failValue;
            if (event.hasValue()) {
                Deprecations.oldTernTag.warn(attribute.context);
                failValue = null;
                attribute = attribute.fulfill(1);
            }
            else if (attribute.hasContext(3)) {
                failValue = attribute.getContextObject(3);
                attribute = attribute.fulfill(3);
            }
            else {
                Debug.echoError("Ternary tag missing 'fail' value!");
                return;
            }
            if (failValue != null) {
                event.setReplacedObject(failValue.getObjectAttribute(attribute));
            }
        }
    }

}
