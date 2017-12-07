/*
 * Copyright 2017 Artem Labazin <xxlabaza@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package feign.form.multipart;

import static feign.form.ContentProcessor.CRLF;

import lombok.val;

import java.io.File;
import java.net.URLConnection;

/**
 *
 * @author Artem Labazin <xxlabaza@gmail.com>
 */
public class ParameterWriter extends AbstractWriter {

  private static String CONTENT_TYPE="Content-Type: ";
  @Override
  public boolean isApplicable (Object value) {
    if (value == null) {
      return false;
    }
    return value instanceof Number ||
         value instanceof String;
  }

  @Override
  protected void write (Output output, String key, Object value) throws Exception {
    String[] keysArr = key.split(";");
    String keyVal = keysArr[0];
    String contentType=CONTENT_TYPE+"text/plain; charset=UTF-8";
    if(keysArr[1] != null){
      contentType=CONTENT_TYPE+keysArr[1];
    }
    val string = new StringBuilder()
            .append("Content-Disposition: form-data; name=\"").append(keyVal).append('"').append(CRLF)
            .append(contentType).append(CRLF)
            .append(CRLF)
            .append(value.toString())
            .toString();

    output.write(string);
  }
}
