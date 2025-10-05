using Kolos.Models;
using Microsoft.Data.SqlClient;

namespace Kolos.Repository;

public class BookRepository : IBookRepository
{
    private readonly IConfiguration _configuration;

    public BookRepository(IConfiguration configuration)
    {
        _configuration = configuration;
    }

    public async Task<bool> DoesBookExist(int id)
    {
        var query = "SELECT 1 FROM Books  WHERE PK = @id";
        
        await using SqlConnection connection = new SqlConnection(_configuration.GetConnectionString("Default"));
        await using SqlCommand command = new SqlCommand();

        command.Connection = connection;
        command.CommandText = query;
        command.Parameters.AddWithValue("@id", id);

        await connection.OpenAsync();
        var result = await command.ExecuteScalarAsync();

        return result is not null;
    }
    public async Task<Book> GetBook(int id)
    {
        var query = @"SELECT books.PK AS PK, title, first_name, last_name FROM books
                        JOIN books_authors ON books.PK = books_authors.FK_book
                        JOIN authors ON authors.PK = books_authors.FK_author
                        WHERE books.PK = @id";
        
        await using SqlConnection connection = new SqlConnection(_configuration.GetConnectionString("Default"));
        await using SqlCommand command = new SqlCommand();

        command.Connection = connection;
        command.CommandText = query;
        command.Parameters.AddWithValue("@id", id);

        await connection.OpenAsync();
        var reader = await command.ExecuteReaderAsync();

        var bookIdOrdinal = reader.GetOrdinal("PK");
        var bookTitleOrdinal = reader.GetOrdinal("title");
        var bookFirstOrdinal = reader.GetOrdinal("first_name");
        var bookLastOrdinal = reader.GetOrdinal("last_name");

        Book book = null;

        while (await reader.ReadAsync())
        {
            if (book is null)
            {
                book = new Book()
                {
                    Id = reader.GetInt32(bookIdOrdinal),
                    Title = reader.GetString(bookTitleOrdinal),
                    Authors = new List<Author>()
                    {
                        new Author()
                        {
                            FirstName = reader.GetString(bookFirstOrdinal),
                            LastName = reader.GetString(bookLastOrdinal)
                        }
                    }
                };
            }
            else
            {
                book.Authors.Add(new Author()
                {
                    FirstName = reader.GetString(bookFirstOrdinal),
                    LastName = reader.GetString(bookLastOrdinal)
                });
            }
        }

        if (book is null)
        {
            throw new Exception();
        }

        return book;
    }
    public async Task<bool> DoesBookExist(string title)
    {
        var query = "SELECT 1 FROM Books  WHERE title = @title";
        
        await using SqlConnection connection = new SqlConnection(_configuration.GetConnectionString("Default"));
        await using SqlCommand command = new SqlCommand();

        command.Connection = connection;
        command.CommandText = query;
        command.Parameters.AddWithValue("@title", title);

        await connection.OpenAsync();
        var result = await command.ExecuteScalarAsync();

        return result is not null;
    }

    public async Task<int> DoesAuthorExist(Author author)
    {
        var query = "SELECT PK FROM authors  WHERE first_na = @first AND last_na = @last";
        
        await using SqlConnection connection = new SqlConnection(_configuration.GetConnectionString("Default"));
        await using SqlCommand command = new SqlCommand();

        command.Connection = connection;
        command.CommandText = query;
        command.Parameters.AddWithValue("@first", author.FirstName);
        command.Parameters.AddWithValue("@last", author.LastName);

        await connection.OpenAsync();
        var result = await command.ExecuteScalarAsync();

        if (result is null)
        {
            return -1;
        }
        
        return Convert.ToInt32(result);    
    }

    public async Task<int> AddBook(string title)
    {
        var query = "INSERT INTO books VALUES (@title); SELECT @@IDENTITY AS ID; ";
        
        await using SqlConnection connection = new SqlConnection(_configuration.GetConnectionString("Default"));
        await using SqlCommand command = new SqlCommand();

        command.Connection = connection;
        command.CommandText = query;
        command.Parameters.AddWithValue("@title", title);

        await connection.OpenAsync();
        var result = await command.ExecuteScalarAsync();
        
        return Convert.ToInt32(result);  
    }

    public async Task<int> AddAuthor(Author author)
    {
        int authorId = await AddAuthor(author);
        if (authorId != -1)
        {
            return authorId;
        }
        
        var query = "INSERT INTO authors VALUES (@first, @last); SELECT @@IDENTITY AS ID; ";
        
        await using SqlConnection connection = new SqlConnection(_configuration.GetConnectionString("Default"));
        await using SqlCommand command = new SqlCommand();

        command.Connection = connection;
        command.CommandText = query;
        command.Parameters.AddWithValue("@first", author.FirstName);
        command.Parameters.AddWithValue("@last", author.LastName);

        await connection.OpenAsync();
        var result = await command.ExecuteScalarAsync();
        
        return Convert.ToInt32(result);      
    }

    public async Task AddAuthorWithBookId(int idBook, int idAuthor)
    {
        var query = "INSERT INTO books_authors VALUES (@idBook, @idAuthor)";
        
        await using SqlConnection connection = new SqlConnection(_configuration.GetConnectionString("Default"));
        await using SqlCommand command = new SqlCommand();

        command.Connection = connection;
        command.CommandText = query;
        command.Parameters.AddWithValue("@idBook", idBook);
        command.Parameters.AddWithValue("@idAuthor", idAuthor);

        await connection.OpenAsync();
        
        await command.ExecuteNonQueryAsync();
    }
}