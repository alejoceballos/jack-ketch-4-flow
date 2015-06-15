/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Alejo Ceballos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package somossuinos.jackketch.transform.unit.reader;

import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import somossuinos.jackketch.transform.reader.Jk4flowReader;
import somossuinos.jackketch.transform.reader.VioletReader;

public class VioletReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testReadInvalidFormat() {
        final Jk4flowReader reader = new VioletReader();

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Invalid file format! Not a compatible Violet 2.1.0 file version.");

        reader.read(new File("src/test/resources/invalid-format.activity.violet.html"));
    }

    @Test
    public void testReadSuccessful() {
        final Jk4flowReader reader = new VioletReader();
        final String text = reader.read(new File("src/test/resources/activity-diagram.activity.violet.html"));
        Assert.assertTrue(StringUtils.isNotBlank(text));
    }

}
