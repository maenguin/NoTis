package com.maenguin.notis.model.notion;

import com.maenguin.notis.model.notion.NotionPage;

public interface NotionPageExtractor {

    NotionPage extractPage(String uri);

}
