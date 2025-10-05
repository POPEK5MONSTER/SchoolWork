using System.Transactions;
using Kolos2.Context;
using Kolos2.Models;
using Microsoft.EntityFrameworkCore;

namespace Kolos2.Services;

public class DbService : IDbService
{
    public Game_Context Context;

    public DbService(Game_Context context)
    {
        Context = context;
    }
    
    public async Task<bool> DoesCharExist(int id)
    {
        var result = await Context.Characters.FindAsync(id);
        if (result is not null)
        {
            return true;
        }

        return false;
    }

    public async Task<object> GetChar(int id)
    {
        var characters = await Context.Characters
            .Include(x => x.CharacterTitles)
            .ThenInclude(x => x.TitleNavigation)
            .Include(x => x.Backpacks)
            .ThenInclude(x => x.ItemNavigation)
            .FirstOrDefaultAsync(x => x.Id == id);

        if (characters is null)
        {
            throw new Exception();
        }

        var result = new
        {
            firstName = characters.FirstName,
            lastName = characters.LastName,
            currentWeight = characters.CurrentWeight,
            maxWeight = characters.MaxWeight,
            backPackItems = characters.Backpacks.Select(e => new
            {
                itemName = e.ItemNavigation.Name,
                itemWeight = e.ItemNavigation.Weight,
                amount = e.Amount
            }),
            titles = characters.CharacterTitles.Select(e => new
            {
                title = e.TitleNavigation.Name,
                acquiredAt = e.AcquiredAt
            })
        };

        return result;
    }

    public async Task<bool> DoesItemExist(int id)
    {
        var result =  await Context.Items.FindAsync(id);
        if (result is not null)
        {
            return true;
        }

        return false;
    }

    public async Task<bool> DoesCharHaveEnoughtWeight(int id, List<int> newIdItems)
    {
        int ammount = 0;
        foreach (var idItem in newIdItems)
        {
            Item? item = await Context.Items.FindAsync(idItem);
            if (item is null)
            {
                throw new Exception();
            }
            ammount += item.Weight;
        }

        Character? character = await Context.Characters.FindAsync(id);

        if (character is null)
        {
            throw new Exception();
        }

        var result = character.CurrentWeight += ammount;
            
        
        return result <= character.MaxWeight;

    }

    public async Task AddItemsToBackpack(int id, List<int> newIdItems)
    {
        using (TransactionScope scope = new TransactionScope(TransactionScopeAsyncFlowOption.Enabled))
        {
            foreach (var idItem in newIdItems)
            {
                Backpack? backpack =
                    await Context.Backpacks.FirstOrDefaultAsync(e =>
                        e.ItemId == idItem && e.CharacterId == id);
                
                if (backpack is null)
                {
                    Context.Backpacks.Add(new Backpack()
                    {
                        ItemId = idItem,
                        CharacterId = id,
                        Amount = 1
                    });
                }
                else
                {
                    backpack.Amount += 1;
                }
                await Context.SaveChangesAsync();
            }

            foreach (var itemId in newIdItems)
            {
                Item? item = await Context.Items.FindAsync(itemId);
                if (item is null)
                {
                    throw new Exception();
                }
            }

            Character? character = await Context.Characters.FindAsync(id);
            if (character is null)
            {
                throw new Exception();
            }
            
            await Context.SaveChangesAsync();
            
            scope.Complete();
        }
    }

    public async Task<List<object>> GetCharactersItems(int id)
    {
        var result = await Context.Backpacks.Where(x => x.CharacterId == id).Select(x => new
        {
            amount = x.Amount,
            itemId = x.ItemId,
            characterId = id
        }).ToListAsync();
        return result.Cast<object>().ToList();
    }
}