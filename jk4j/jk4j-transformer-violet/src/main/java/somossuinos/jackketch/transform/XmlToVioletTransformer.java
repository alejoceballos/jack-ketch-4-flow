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

package somossuinos.jackketch.transform;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang3.StringUtils;
import somossuinos.jackketch.transform.violet.VltWorkflow;

public class XmlToVioletTransformer implements Jk4flowTransformer<String, VltWorkflow> {

    @Override
    public VltWorkflow transform(final String xml) {
        if (StringUtils.isBlank(xml)) {
            throw new RuntimeException("XML should not be blank");
        }


        Reader reader = null;

        try {
            reader = new StringReader(xml);
            final JAXBContext context = JAXBContext.newInstance(VltWorkflow.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();

            return (VltWorkflow) unmarshaller.unmarshal(reader);

        } catch (JAXBException jaxbe) {
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            }

            throw new RuntimeException(jaxbe);
        }
    }

}
