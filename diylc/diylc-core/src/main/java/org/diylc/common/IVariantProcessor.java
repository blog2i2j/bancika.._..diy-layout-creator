/*

    DIY Layout Creator (DIYLC).
    Copyright (c) 2009-2025 held jointly by the individual authors.

    This file is part of DIYLC.

    DIYLC is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    DIYLC is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with DIYLC.  If not, see <http://www.gnu.org/licenses/>.

*/
package org.diylc.common;

import java.io.IOException;
import java.util.List;

import org.diylc.core.Template;

public interface IVariantProcessor {

  public static final String TEMPLATES_KEY = "templates";
  public static final String DEFAULT_TEMPLATES_KEY = "defaultTemplates";
  public static final String DEFAULT_TEMPLATES_IMPORT_DATE_KEY = "defaultTemplatesImportDate";

  void saveSelectedComponentAsVariant(String variantName);

  List<Template> getVariantsFor(ComponentType type);

  List<Template> getVariantsForSelection();

  void applyVariantToSelection(Template variant);

  void deleteVariant(ComponentType type, String variantName);

  void setDefaultVariant(ComponentType type, String variantName);
  
  int importVariants(String fileName) throws IOException;
  
  String getDefaultVariant(ComponentType type);

  public class VariantAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

  }
}
