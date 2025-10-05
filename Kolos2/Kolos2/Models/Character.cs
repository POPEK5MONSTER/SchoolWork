using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Kolos2.Models;

[Table("characters")]
public class Character
{
    [Key] public int Id { get; set; }

    [Required] [MaxLength(50)] public string FirstName { get; set; } = null!;
    
    [Required] [MaxLength(120)] public string LastName { get; set; } = null!;

    [Required] public int CurrentWeight { get; set; }
    
    [Required] public int MaxWeight { get; set; }
    
    public List<Character_title> CharacterTitles { get; set; } = new List<Character_title>();

    public List<Backpack> Backpacks { get; set; } = new List<Backpack>();

}