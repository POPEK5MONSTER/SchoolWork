namespace Kolos.Models;

public class NewBook
{ 
    public string Title { get; set; } = string.Empty;
    public List<Author> Authors { get; set; } = null!;
}
