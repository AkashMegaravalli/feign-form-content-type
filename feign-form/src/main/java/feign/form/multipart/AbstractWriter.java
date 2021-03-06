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

import java.net.URLConnection;
import lombok.SneakyThrows;
import lombok.val;

/**
 *
 * @author Artem Labazin <xxlabaza@gmail.com>
 */
public abstract class AbstractWriter implements Writer {

  @Override
  public void write (Output output, String boundary, String key, Object value) throws Exception {
    output.write("--").write(boundary).write(CRLF);
    write(output, key, value);
    output.write(CRLF);
  }

  /**
   * Writes data for it's children.
   *
   * @param output  output writer.
   * @param key     name for piece of data.
   * @param value   piece of data.
   */
  protected void write (Output output, String key, Object value) throws Exception {
  }

  /**
   * Writes file's metadata.
   *
   * @param output      output writer.
   * @param name        name for piece of data.
   * @param fileName    file name.
   * @param contentType type of file content. May be the {@code null}, in that case it will be determined by file name.
   */
  @SneakyThrows
  protected void writeFileMetadata (Output output, String name, String fileName, String contentType) {
    val contentDesposition = new StringBuilder()
        .append("Content-Disposition: form-data; name=\"").append(name).append("\"; ")
        .append("filename=\"").append(fileName).append("\"")
        .toString();

    if (contentType == null) {
      contentType = fileName != null
                    ? URLConnection.guessContentTypeFromName(fileName)
                    : "application/octet-stream";
    }

    val string = new StringBuilder()
        .append(contentDesposition).append(CRLF)
        .append("Content-Type: ").append(contentType).append(CRLF)
        .append("Content-Transfer-Encoding: binary").append(CRLF)
        .append(CRLF)
        .toString();

    output.write(string);
  }
}
