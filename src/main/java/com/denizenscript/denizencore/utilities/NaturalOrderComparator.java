package com.denizenscript.denizencore.utilities;

/*
NaturalOrderComparator.java -- Perform 'natural order' comparisons of strings in Java.
Copyright (C) 2003 by Pierre-Luc Paour <natorder@paour.com>
Based on the C version by Martin Pool, of which this is more or less a straight conversion.
Copyright (C) 2000 by Martin Pool <mbp@humbug.org.au>
This software is provided 'as-is', without any express or implied
warranty. In no event will the authors be held liable for any damages
arising from the use of this software.
Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:
1. The origin of this software must not be misrepresented; you must not
claim that you wrote the original software. If you use this software
in a product, an acknowledgment in the product documentation would be
appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be
misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
*/

// Note additional alterations prefixed 'mcmonkey'

import com.denizenscript.denizencore.objects.ArgumentHelper;

import java.util.Comparator;

public class NaturalOrderComparator implements Comparator {
    public static AsciiMatcher DIGIT_MATCHER = new AsciiMatcher(AsciiMatcher.DIGITS); // mcmonkey - micro-optimization

    int compareRight(String a, String b) {
        int bias = 0;
        int ia = 0;
        int ib = 0;
        // The longest run of digits wins. That aside, the greatest
        // value wins, but we can't know that it will until we've scanned
        // both numbers to know that they have the same magnitude, so we
        // remember it in BIAS.
        for (; ; ia++, ib++) {
            char ca = charAt(a, ia);
            char cb = charAt(b, ib);
            if (!DIGIT_MATCHER.isMatch(ca) && !DIGIT_MATCHER.isMatch(cb)) {
                return bias;
            }
            else if (!DIGIT_MATCHER.isMatch(ca)) {
                return -1;
            }
            else if (!DIGIT_MATCHER.isMatch(cb)) {
                return +1;
            }
            else if (ca < cb) {
                if (bias == 0) {
                    bias = -1;
                }
            }
            else if (ca > cb) {
                if (bias == 0) {
                    bias = +1;
                }
            }
            else if (ca == 0 && cb == 0) {
                return bias;
            }
        }
    }

    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        else if (o1 == null) {
            return 1;
        }
        else if (o2 == null) {
            return -1;
        }
        String a = o1.toString();
        String b = o2.toString();
        int ia = 0, ib = 0;
        int nza, nzb;
        char ca, cb;
        int result;
        if (ArgumentHelper.matchesDouble(a) && ArgumentHelper.matchesDouble(b)) { // mcmonkey - improve number handling
            try {
                double numA = Double.parseDouble(a);
                double numB = Double.parseDouble(b);
                return Double.compare(numA, numB);
            }
            catch (NumberFormatException ex) {
                // Ignore
            }
        }
        while (true) {
            // only count the number of zeroes leading the last number compared
            nza = nzb = 0;
            ca = charAt(a, ia);
            cb = charAt(b, ib);
            // skip over leading spaces or zeros
            while (Character.isSpaceChar(ca) || ca == '0') {
                if (ca == '0') {
                    nza++;
                }
                else {
                    // only count consecutive zeroes
                    nza = 0;
                }
                ca = charAt(a, ++ia);
            }
            while (Character.isSpaceChar(cb) || cb == '0') {
                if (cb == '0') {
                    nzb++;
                }
                else {
                    // only count consecutive zeroes
                    nzb = 0;
                }
                cb = charAt(b, ++ib);
            }
            // process run of digits
            if (DIGIT_MATCHER.isMatch(ca) && DIGIT_MATCHER.isMatch(cb)) {
                if ((result = compareRight(a.substring(ia), b.substring(ib))) != 0) {
                    return result;
                }
            }
            if (ca == 0 && cb == 0) {
                // The strings compare the same. Perhaps the caller
                // will want to call strcmp to break the tie.
                return nza - nzb;
            }
            if (ca < cb) {
                return -1;
            }
            else if (ca > cb) {
                return +1;
            }
            ++ia;
            ++ib;
        }
    }

    static char charAt(String s, int i) {
        if (i >= s.length()) {
            return 0;
        }
        else {
            return s.charAt(i);
        }
    }
}
