package ai_with_spring;

import org.apache.logging.log4j.message.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe-chef")
public class RecipeChefController {

    private final ChatClient chatClient;
    private final List<Message> conversation;

    public RecipeChefController(ChatClient.Builder chatClientBuilder, List<Message> conversation) {
        this.chatClient = chatClientBuilder.build();
        this.conversation = conversation;
        final String systemMessageString = """
        Suggest sea food recipe.
        If someone asks about something else, just say I don't know.
        """;
        final SystemMessage systemMessage = new SystemMessage(systemMessageString);
        this.conversation.add(systemMessage);
    }

    @GetMapping("/suggest-recipe")
    public String suggestRecipe(
            @RequestParam(
                    name = "message",
                    defaultValue = "Suggest a recipe for dinner"
            ) String message){
        final Message userMessage = new UserMessage(message);
        this.conversation.add(userMessage);
        String modelResponse = this.chatClient.prompt()
                .messages(this.conversation)
                .call()
                .content();
        final Message assistantMessage = new AssistantMessage(modelResponse);
        this.conversation.add(assistantMessage);
        return modelResponse;
    }

}
