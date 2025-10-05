using Kolos2.Models;
using Microsoft.EntityFrameworkCore;

namespace Kolos2.Context;

public class Game_Context : DbContext
{
    protected Game_Context() {}

    public Game_Context(DbContextOptions options) : base(options) {}
    
    public DbSet<Title> Titles { get; set; }
    public DbSet<Item> Items { get; set; }
    public DbSet<Character> Characters { get; set; }
    public DbSet<Backpack> Backpacks { get; set; }
    public DbSet<Character_title> CharacterTitles { get; set; }
    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        
        modelBuilder.Entity<Title>().HasData(
            new Title()
            {
                Id = 1, 
                Name = "Title1"
            },
            new Title()
            {
                Id = 2,
                Name = "Title2"
            }
            );
        modelBuilder.Entity<Item>().HasData(
            new Item()
            {
                Id = 1,
                Name = "Maczeta",
                Weight = 10
            },
            new Item()
            {
                Id = 2,
                Name = "Miecz",
                Weight = 20
            }
        );
        modelBuilder.Entity<Character>().HasData(
            new Character()
            {
                Id = 1,
                FirstName = "Jamal",
                LastName = "Curry",
                CurrentWeight = 10,
                MaxWeight = 100
            },
            new Character()
            {
                Id = 2,
                FirstName = "Wiesiek",
                LastName = "Jordan",
                CurrentWeight = 20,
                MaxWeight = 80
            }
        );

        modelBuilder.Entity<Backpack>().HasData(
            new Backpack()
            {
                CharacterId = 1,
                ItemId = 1,
                Amount = 1
            },
            new Backpack()
            {
                CharacterId = 2,
                ItemId = 2,
                Amount = 2
            }
            );
        
        modelBuilder.Entity<Character_title>().HasData(
            new Character_title()
            {
                CharacterId = 1,
                TitleId = 1,
                AcquiredAt = new DateTime(2020, 1, 1)
            },
            new Character_title()
            {
                CharacterId = 2,
                TitleId = 2,
                AcquiredAt = new DateTime(2021, 2, 2)
            }
        );
        base.OnModelCreating(modelBuilder);
    }
}