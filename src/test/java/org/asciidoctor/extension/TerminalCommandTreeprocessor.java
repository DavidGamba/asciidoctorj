package org.asciidoctor.extension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.Document;

public class TerminalCommandTreeprocessor extends Treeprocessor {

	private Document document;
	
    public TerminalCommandTreeprocessor(Map<String, Object> config) {
        super(config);
    }

    @Override
    public void process(Document document) {

    	this.document = document;
    	
        final List<Block> blocks = this.document.blocks();

        for (int i = 0; i < blocks.size(); i++) {
            final Block currentBlock = blocks.get(i);
            List<String> lines = currentBlock.lines();
            if (lines.size() > 0 && lines.get(0).startsWith("$")) {
                blocks.set(
                        i, convertToTerminalListing(currentBlock));
                        
            }

        }
    }

    public Block convertToTerminalListing(Block block) {

        Map<String, Object> attributes = block.attributes();
        attributes.put("role", "terminal");
        StringBuilder resultLines = new StringBuilder();

        List<String> lines = block.lines();

        for (String line : lines) {
            if (line.startsWith("$")) {
                resultLines.append("<span class=\"command\">")
                        .append(line.substring(2, line.length()))
                        .append("</command");
            } else {
                resultLines.append(line);
            }
        }

        return createBlock(this.document, "listing", Arrays.asList(resultLines.toString()), attributes,
                new HashMap<Object, Object>());
    }

}
