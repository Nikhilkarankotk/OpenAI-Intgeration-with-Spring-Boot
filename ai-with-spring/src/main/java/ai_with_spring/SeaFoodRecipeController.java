package ai_with_spring;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes/seafood")
public class SeaFoodRecipeController {

    private final ChatClient chatClient;

    public SeaFoodRecipeController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/suggest-recipe")
    public String suggestRecipe(
            @RequestParam(
                    name = "message",
                    defaultValue = "S"
            ) String message){
        final String systemMessage = """
        Suggest sea food recipe.
        If someone asks about something else, just say I don't know.
        """;
        return this.chatClient.prompt()
                .system(c -> c.text(systemMessage))
                .user(message)
                .call()
                .content();
    }

}
