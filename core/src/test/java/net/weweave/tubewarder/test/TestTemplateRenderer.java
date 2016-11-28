package net.weweave.tubewarder.test;

import net.weweave.tubewarder.exception.TemplateModelException;
import net.weweave.tubewarder.util.TemplateRenderer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

public class TestTemplateRenderer {
    @Test
    public void testSimpleMapModel() throws Exception {
        String template = "Hello {{firstname}} {{lastname}}!";
        Map<String, Object> model = new HashMap<>();
        model.put("firstname", "John");
        model.put("lastname", "Doe");
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model);
        Assert.assertEquals("Hello John Doe!", result);
    }

    @Test
    public void testSimpleJsonModel() throws Exception {
        String template = "Hello {{firstname}} {{lastname}}!";
        JSONObject model = new JSONObject();
        model.put("firstname", "John");
        model.put("lastname", "Doe");
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model.toString());
        Assert.assertEquals("Hello John Doe!", result);
    }

    @Test(expected = TemplateModelException.class)
    public void testCorruptedJsonModel() throws Exception {
        String template = "Hello {{firstname}} {{lastname}}!";
        String json = "{\"firstname': 'test'}";
        TemplateRenderer renderer = new TemplateRenderer();
        renderer.render(template, json);
    }

    @Test
    public void testMissingTemplateParameterJsonModel() throws Exception {
        String template = "Hello {{firstname}} {{lastname}}!";
        JSONObject model = new JSONObject();
        model.put("firstname", "John");
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model.toString());
        Assert.assertEquals("Hello John !", result);
    }

    @Test
    public void testConditionalJson() throws Exception {
        String template = "Hello {{#if male}}Mr. {{name}}{{else}}Mrs. {{name}}{{/if}}!";
        JSONObject model = new JSONObject();
        model.put("name", "Smith");
        model.put("male", true);
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model.toString());
        Assert.assertEquals("Hello Mr. Smith!", result);
    }

    @Test
    public void testConditionalJsonElse() throws Exception {
        String template = "Hello {{#if male}}Mr. {{name}}{{else}}Mrs. {{name}}{{/if}}!";
        JSONObject model = new JSONObject();
        model.put("name", "Smith");
        model.put("male", false);
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model.toString());
        Assert.assertEquals("Hello Mrs. Smith!", result);
    }

    @Test
    public void testWrongTypeJson() throws Exception {
        String template = "Hello {{#if male}}Mr. {{name}}{{/if}}!";
        JSONObject model = new JSONObject();
        model.put("name", "Smith");
        model.put("male", "0");
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model.toString());
        Assert.assertEquals("Hello Mr. Smith!", result);
    }

    @Test
    public void testConditionalJsonMissingParameter() throws Exception {
        String template = "Hello {{#if male}}Mr. {{name}}{{/if}}!";
        JSONObject model = new JSONObject();
        model.put("name", "Smith");
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model.toString());
        Assert.assertEquals("Hello !", result);
    }

    @Test
    public void testListJson() throws Exception {
        String template = "Hello{{#each people}} -> {{firstname}} {{lastname}}{{/each}}!";
        JSONArray people = new JSONArray();
        JSONObject p1 = new JSONObject();
        p1.put("firstname", "John");
        p1.put("lastname", "Doe");
        people.put(p1);
        JSONObject p2 = new JSONObject();
        p2.put("firstname", "Max");
        p2.put("lastname", "Miller");
        people.put(p2);
        JSONObject model = new JSONObject();
        model.put("people", people);
        TemplateRenderer renderer = new TemplateRenderer();
        String result = renderer.render(template, model.toString());
        Assert.assertEquals("Hello -> John Doe -> Max Miller!", result);
    }
}
