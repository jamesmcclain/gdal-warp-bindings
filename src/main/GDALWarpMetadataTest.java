/*
 * Copyright 2019-2021 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.azavea.gdal.GDALWarp;
import java.util.Arrays;
import java.util.Random;

class GDALWarpMetadataTest {

    private static String options[] = { /* */
            "-tap", "-tr", "5", "7", /* */
            "-r", "bilinear", /* */
            "-t_srs", "epsg:3857", /* */
            "-co", "BLOCKXSIZE=512", "-co", "BLOCKYSIZE=512" /* */
    };

    public static void main(String[] args) throws Exception {
        long token = GDALWarp.get_token(args[0], options);
        long before, after;
        int len = 0;

        // Noop
        {
            len = 0;
            before = System.currentTimeMillis();
            for (int j = 0; j < (1 << 18); ++j) {
                len += GDALWarp.noop(token, GDALWarp.SOURCE, 0);
            }
            after = System.currentTimeMillis();
            System.out.println(after - before + " " + len);
        }

        // Domain List Metadata ThreadLocal
        {
            len = 0;
            before = System.currentTimeMillis();
            String[][] domain_list = new String[1][0];
            for (int j = 0; j < (1 << 18); ++j) {
                GDALWarp.get_metadata_domain_list(token, GDALWarp.SOURCE, 0, 0, domain_list);
                for (int i = 0; i < domain_list[0].length; ++i) {
                    len = len + domain_list[0][i].length();
                }
            }
            after = System.currentTimeMillis();
            System.out.println(after - before + " " + len);
        }

        // Domain List Metadata Reallocate 16⨯1024
        {
            // Domain List Metadata
            before = System.currentTimeMillis();
            for (int j = 0; j < (1 << 18); ++j) {
                byte[][] domain_list1 = new byte[1 << 4][1 << 10];
                GDALWarp.get_metadata_domain_list(token, GDALWarp.SOURCE, 0, 0, domain_list1);
                for (int i = 0; i < domain_list1.length; ++i) {
                    String str = new String(domain_list1[i], "UTF-8").trim();
                    if (str.length() > 0) {
                        len = len + str.length();
                    } else {
                        break;
                    }
                }
            }
            after = System.currentTimeMillis();
            System.out.println(after - before + " " + len);
        }

        // Domain List Metadata Reallocate 1024⨯1024
        {
            // Domain List Metadata
            before = System.currentTimeMillis();
            for (int j = 0; j < (1 << 18); ++j) {
                byte[][] domain_list1 = new byte[1 << 10][1 << 10];
                GDALWarp.get_metadata_domain_list(token, GDALWarp.SOURCE, 0, 0, domain_list1);
                for (int i = 0; i < domain_list1.length; ++i) {
                    String str = new String(domain_list1[i], "UTF-8").trim();
                    if (str.length() > 0) {
                        len = len + str.length();
                    } else {
                        break;
                    }
                }
            }
            after = System.currentTimeMillis();
            System.out.println(after - before + " " + len);
        }

        // Domain List Metadata Preallocate
        {
            // Domain List Metadata
            byte[][] domain_list1 = new byte[1 << 10][1 << 10];
            before = System.currentTimeMillis();
            for (int j = 0; j < (1 << 18); ++j) {
                GDALWarp.get_metadata_domain_list(token, GDALWarp.SOURCE, 0, 0, domain_list1);
                for (int i = 0; i < domain_list1.length; ++i) {
                    String str = new String(domain_list1[i], "UTF-8").trim();
                    if (str.length() > 0) {
                        len = len + str.length();
                    } else {
                        break;
                    }
                }
            }
            after = System.currentTimeMillis();
            System.out.println(after - before + " " + len);
        }

        GDALWarp.deinit();
        return;
    }
}
